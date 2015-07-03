package agents

import domain.AgentType
import domain.AgentType.AgentType
import events.EventType.EventType
import events.{EventDispatcher, EventType}

/**
 * Created by Felipe on 19/06/2015.
 */
class TransportWorkerAgent extends AbstractResourceAgent() {

  var hasSteelSheet = false

  private def AddNewSteelSheetIncomeBehaviour(): Unit = {
    MessageModule.receive(this, "newSteelSheet", (msg) => {
      hasSteelSheet = true
    })
  }

  private def addSearchCNCMachineAndDeliverSteelSheetBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (hasSteelSheet) {
        val cutMachineWithAvailability = AgentsModule.getCUTMachineWithAvailability
        cutMachineWithAvailability match {
          case Some(a) => {
            changeToWorking()
            doWait(configurationData.transportTimeToCNC)
            MessageModule.send(this, a.getLocalName, "newSheetToQueue")
            EventDispatcher.Dispatch(EventType.SteelSheetTransported)
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
