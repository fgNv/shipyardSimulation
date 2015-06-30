package agents

import domain.AgentType
import domain.AgentType.AgentType
import domain.product.SteelSheet
import events.EventType
import events.EventType.EventType
import jade.core.behaviours.CyclicBehaviour
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCCutMachineAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCCutMachineAgent
  }

  private val configurationData = CompositionRoot.configurationDataFactory.getConfigurationData()

  var steelSheetQueue = new ListBuffer[SteelSheet]
  val maxQueueCapacity = CompositionRoot.configurationDataFactory.getConfigurationData().cncQueueCapacity

  def hasAvailabilityInQueue = {
    steelSheetQueue.length < maxQueueCapacity
  }

  private def addNewSheetToQueueBehaviour(): Unit = {
    addBehaviour(new CyclicBehaviour() {
      override def action(): Unit = {
        val message = myAgent.receive()
        if (message == null || message.getContent() != "newSheetToQueue")
          return

        steelSheetQueue.append(new SteelSheet())
        changeToWorking()
      }
    })
  }

  private def addProcessSteelSheetBehaviour(): Unit = {
    addBehaviour(new CyclicBehaviour() {
      override def action(): Unit = {
        if(steelSheetQueue.length == 0)
          return

        val cutAuxiliary = AgentsModule.getIdleAgent(AgentType.CutSectorAncillaryAgent)

        cutAuxiliary match {
          case Some(agent) => MessageModule.send(myAgent, agent, "moveSteelSheetToCNC")
          case None => return
        }
      }
    })
  }

  override def activeEvent: EventType = EventType.CNCCutMachineActive

  override def idleEvent: EventType = EventType.CNCCutMachineIdle

  override def createdEvent: EventType = EventType.CNCCutMachineCreated

  override def setup(): Unit = {
    super.setup()

    addNewSheetToQueueBehaviour()
  }
}
