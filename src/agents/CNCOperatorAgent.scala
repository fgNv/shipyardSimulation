package agents

import domain.AgentType
import domain.AgentType._
import events.EventType
import events.EventType.EventType

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
}
