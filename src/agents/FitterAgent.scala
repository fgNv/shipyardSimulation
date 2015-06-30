package agents

import domain.AgentType
import domain.AgentType.AgentType
import events.EventType
import events.EventType.EventType

/**
 * Created by Felipe on 19/06/2015.
 */
class FitterAgent extends AbstractResourceAgent(){
  override def getAgentType(): AgentType = {
    AgentType.FitterAgent
  }

  override def activeEvent: EventType = EventType.FitterActive

  override def idleEvent: EventType = EventType.FitterIdle

  override def createdEvent: EventType = EventType.FitterCreated
}
