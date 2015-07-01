package agents

import domain.AgentType
import domain.AgentType.AgentType
import events.EventType
import events.EventType.EventType
import object_graph.CompositionRoot

/**
 * Created by Felipe on 19/06/2015.
 */
class TransportWorkerAgent extends AbstractResourceAgent() {

  private val configurationData = CompositionRoot.configurationDataFactory.getConfigurationData()

  private var hasSteelSheet = false

  private def AddNewSteelSheetIncomeBehaviour(): Unit = {
    MessageModule.receive(this, "newSteelSheet", (msg) => {
      changeToWorking()
      hasSteelSheet = true
      MessageModule.send(this, msg.getSender().getLocalName, "steelSheetDelivered")
    })
  }

  private def addSearchCNCMachineAndDeliverSteelSheetBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (hasSteelSheet) {
        val cutMachineWithAvailability = AgentsModule.getCUTMachineWithAvailability
        cutMachineWithAvailability match {
          case Some(a) => {
            MessageModule.send(this, a, "newSheetToQueue")
            Thread.sleep(configurationData.transportTimeToCNC)
            changeToIdle()
            hasSteelSheet = false
          }
          case None => ()
        }
      }
    })
  }

  override def getAgentType(): AgentType = {
    AgentType.TransportWorkerAgent
  }

  override def setup = {
    super.setup()

    AddNewSteelSheetIncomeBehaviour()
    addSearchCNCMachineAndDeliverSteelSheetBehaviour()
  }

  override def activeEvent: EventType = EventType.TransportWorkerActive

  override def idleEvent: EventType = EventType.TransportWorkerIdle

  override def createdEvent: EventType = EventType.TransportWorkerCreated
}
