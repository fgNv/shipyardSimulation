package agents

import domain.AgentType.AgentType
import domain.product.SteelSheet
import domain.{AgentType, ResourceState}
import events.EventType.EventType
import events.{EventDispatcher, EventType}

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCCutMachineAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCCutMachineAgent
  }

  def getItemsInQueueCount : Int = {
    steelSheetQueue.length
  }

  private val steelSheetQueue = new ListBuffer[SteelSheet]
  private val maxQueueCapacity = configurationData.cncQueueCapacity
  private var hasSteelSheetToProcess = false
  private var hasPiecesToBeMoved = false
  private var tryingToMoveFromQueueToCNC = false
  private var tryingToMovePiecesFromCNC = false

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
      if (steelSheetQueue.length > 0 && !hasSteelSheetToProcess && !hasPiecesToBeMoved && !tryingToMoveFromQueueToCNC) {
        tryingToMoveFromQueueToCNC = true
        val cutAuxiliary = AgentsModule.getIdleAgent(AgentType.CutSectorAncillaryAgent)

        cutAuxiliary match {
          case Some(agent) => {
            MessageModule.send(this, agent.getLocalName, "moveSteelSheetToCNC")
            doWait(configurationData.timeForMaterialMovingInCNC)
            EventDispatcher.Dispatch(EventType.SteelSheetMovedFromCNCQueueToProcess)
            hasSteelSheetToProcess = true
            tryingToMoveFromQueueToCNC = false
            steelSheetQueue.remove(0)
          }
          case None => tryingToMoveFromQueueToCNC = false
        }
      }
    })
  }

  private def addLookForCNCOperatorAndProcessBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (hasSteelSheetToProcess && getResourceState() == ResourceState.Idle) {

        val cncOperator = AgentsModule.getIdleAgent(AgentType.CNCOperatorAgent)
        cncOperator match {
          case Some(agent) => {
            MessageModule.send(this, agent.getLocalName, "processSteelSheet")
            changeToWorking()
            AgentsModule.addWakerBehaviour(this,configurationData.cutTime,() =>{
              changeToIdle()
              hasPiecesToBeMoved = true
              hasSteelSheetToProcess = false
              (0 until configurationData.partsFromSheet.toInt).foreach(u => EventDispatcher.Dispatch(EventType.PartProduced))
            })
          }
          case None => ()
        }
      }
    })
  }

  private def addMovePiecesBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (hasPiecesToBeMoved && !tryingToMovePiecesFromCNC) {
        tryingToMovePiecesFromCNC = true
        val cutAncillary = AgentsModule.getIdleAgent(AgentType.CutSectorAncillaryAgent)
        cutAncillary match {
          case Some(agent) => {
            MessageModule.send(this, agent.getLocalName, "movePiecesFromCNC")
            (0 until configurationData.partsFromSheet.toInt).foreach(u => EventDispatcher.Dispatch(EventType.PieceMovedFromCNC))
            tryingToMovePiecesFromCNC = false
            hasPiecesToBeMoved = false
          }
          case None => tryingToMovePiecesFromCNC = false
        }
      }
    })
  }

  private def addReceivePiecesMovedConfirmation(): Unit = {
    MessageModule.receive(this, "partsMovedFromCNC", (m) => {
      hasPiecesToBeMoved = false
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
    addMovePiecesBehaviour()
    addReceivePiecesMovedConfirmation
  }
}
