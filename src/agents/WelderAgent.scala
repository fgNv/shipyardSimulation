package agents

import domain.AgentType
import domain.AgentType.AgentType
import events.EventType
import events.EventType.EventType

/**
 * Created by Felipe on 19/06/2015.
 */
class WelderAgent extends AbstractResourceAgent(){
  override def getAgentType(): AgentType = {
    AgentType.WelderAgent
  }

  override def activeEvent: EventType = EventType.WelderActive

  override def idleEvent: EventType = EventType.WelderIdle

  override def createdEvent: EventType = EventType.WelderCreated
}
