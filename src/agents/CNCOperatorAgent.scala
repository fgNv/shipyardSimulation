package agents

import domain.AgentType
import domain.AgentType._
import events.EventType.EventType
import events.{EventDispatcher, EventType}

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCOperatorAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCOperatorAgent
  }

  override def activeEvent: EventType = EventType.CNCOperatorActive

  override def idleEvent: EventType = EventType.CNCOperatorIdle

  override def createdEvent: EventType = EventType.CNCOperatorCreated

  private def addProcessSteelSheetBehaviour(): Unit = {
    MessageModule.receive(this, "processSteelSheet", msg => {

      changeToWorking()
      doWait(configurationData.cutTime)
      changeToIdle()
      EventDispatcher.Dispatch(EventType.SteelSheetProcessed)
      MessageModule.send(this, msg.getSender.getLocalName, "steelSheetProcessDone")
    })
  }

  override def setup(): Unit = {
    super.setup()
    addProcessSteelSheetBehaviour()
  }
}
