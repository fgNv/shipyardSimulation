package agents

import domain.AgentType
import domain.AgentType._

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCOperatorAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCOperatorAgent
  }
}
