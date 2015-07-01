package agents

import domain.AgentType
import domain.AgentType.AgentType
import domain.product.SteelSheet
import events.EventType.EventType
import events.{EventDispatcher, EventType}
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCCutMachineAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCCutMachineAgent
  }

  private val steelSheetQueue = new ListBuffer[SteelSheet]
  private val maxQueueCapacity = CompositionRoot.configurationDataFactory.getConfigurationData().cncQueueCapacity
  private var hasSteelSheetToProcess = false
  private var hasPiecesToBeMoved = false

  def hasAvailabilityInQueue = {
    steelSheetQueue.length < maxQueueCapacity
  }

  private def addNewSheetToQueueBehaviour(): Unit = {
    MessageModule.receive(this, "newSheetToQueue", msg => {
      steelSheetQueue.append(new SteelSheet())
      EventDispatcher.Dispatch(EventType.SteelSheetAddedToCNCQueue)
    })
  }

  private def addMoveSteelSheetFromQueueToCNCBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (steelSheetQueue.length > 0 && !hasSteelSheetToProcess && !hasPiecesToBeMoved) {

        val cutAuxiliary = AgentsModule.getIdleAgent(AgentType.CutSectorAncillaryAgent)

        cutAuxiliary match {
          case Some(agent) => {
            changeToWorking()
            MessageModule.send(this, agent, "moveSteelSheetToCNC")
            EventDispatcher.Dispatch(EventType.SteelSheetMovedFromCNCQueueToProcess)
            steelSheetQueue.remove(0)
            hasSteelSheetToProcess = true
          }
          case None => ()
        }
      }
    })
  }

  private def addLookForCNCOperatorAndProcessBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (hasSteelSheetToProcess) {
        val cncOperator = AgentsModule.getIdleAgent(AgentType.CNCOperatorAgent)

        cncOperator match {
          case Some(agent) => {
            MessageModule.send(this, agent, "processSteelSheet")
          }
          case None => ()
        }
      }
    })
  }

  private def addSteelSheetProcessDoneBehaviour(): Unit = {
    MessageModule.receive(this, "steelSheetProcessDone", (msg) => {
      hasPiecesToBeMoved = true
      hasSteelSheetToProcess = false
    })
  }

  private def addMovePiecesBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (hasPiecesToBeMoved) {
        val cutAncillary = AgentsModule.getIdleAgent(AgentType.CutSectorAncillaryAgent)
        cutAncillary match {
          case Some(agent) => MessageModule.send(this, agent, "movePiecesFromCNC")
          case None => ()
        }
      }
    })
  }

  private def addReceivePiecesMovedConfirmation(): Unit = {
    MessageModule.receive(this, "partsMovedFromCNC", (m) => {
      hasPiecesToBeMoved = false
      changeToIdle()
      MessageModule.send(this, "deliveryAgent", "partsFromSheetProduced")
    })
  }

  override def activeEvent: EventType = EventType.CNCCutMachineActive

  override def idleEvent: EventType = EventType.CNCCutMachineIdle

  override def createdEvent: EventType = EventType.CNCCutMachineCreated

  override def setup(): Unit = {
    super.setup()

    addNewSheetToQueueBehaviour()
    addMoveSteelSheetFromQueueToCNCBehaviour()
    addLookForCNCOperatorAndProcessBehaviour()
    addSteelSheetProcessDoneBehaviour()
    addMovePiecesBehaviour()
    addReceivePiecesMovedConfirmation
  }
}
