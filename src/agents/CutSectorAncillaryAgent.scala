package agents

import domain.AgentType
import domain.AgentType._
import events.EventType
import events.EventType.EventType

/**
 * Created by Felipe on 19/06/2015.
 */
class CutSectorAncillaryAgent extends AbstractResourceAgent(){
  override def getAgentType(): AgentType = {
    AgentType.CutSectorAncillaryAgent
  }

  override def activeEvent: EventType = EventType.CutSectorAncillaryActive

  override def idleEvent: EventType = EventType.CutSectorAncillaryIdle

  override def createdEvent: EventType = EventType.CutSectorAncillaryCreated
}
