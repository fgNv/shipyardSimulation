package domain

import domain.AgentType._

/**
 * Created by Felipe on 26/06/2015.
 */
object AgentModule {

  def getAgentClasseName(agentType: AgentType): String = {
    agentType match {
      case AgentType.CNCCutMachineAgent => "agents.CNCCutMachineAgent"
      case AgentType.CNCOperatorAgent => "agents.CNCOperatorAgent"
      case AgentType.CutSectorAncillaryAgent => "agents.CutSectorAncillaryAgent"
      case AgentType.FitterAgent => "agents.FitterAgent"
      case AgentType.TransportWorkerAgent => "agents.TransportWorkerAgent"
      case AgentType.WelderAgent => "agents.WelderAgent"
    }
  }

  private def buildAgentCreationString(agentType : AgentType, configurationData: ConfigurationData) : String = {
    val quantity = configurationData.getResourceQuantity(agentType)
    val agentClassName = getAgentClasseName(agentType)
    (0 until quantity.toInt).map(u => agentClassName + "|" + u + ":" + agentClassName + ";")
                      .reduce((acc, i) => acc + i)
  }

  def buildAgentsCreationString(configurationData: ConfigurationData): String = {
    val _buildAgentCreationString = buildAgentCreationString(_ : AgentType, configurationData)
    AgentType.values.map(_buildAgentCreationString).reduce((acc, i) => acc + i)
  }
}
