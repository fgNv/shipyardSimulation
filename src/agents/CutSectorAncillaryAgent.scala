package agents

import domain.AgentType
import domain.AgentType._

/**
 * Created by Felipe on 19/06/2015.
 */
class CutSectorAncillaryAgent extends AbstractAgent(){
  override def getAgentType(): AgentType = {
    AgentType.CutSectorAncillaryAgent
  }
}
