package agents

import domain.AgentType
import domain.AgentType.AgentType

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCCutMachineAgent extends AbstractAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCCutMachineAgent
  }
}