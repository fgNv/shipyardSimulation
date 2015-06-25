package agents

import domain.AgentType
import domain.AgentType.AgentType
import jade.core.Agent
import object_graph.CompositionRoot

/**
 * Created by Felipe on 25/06/2015.
 */
class InitializerAgent extends Agent {

  val configs = CompositionRoot.configurationDataFactory.getConfigurationData()

  private def getAgentClasseName(agentType: AgentType): String = {
    agentType match {
      case AgentType.CNCCutMachineAgent => "agents.CNCCutMachineAgent"
      case AgentType.CNCOperatorAgent => "agents.CNCOperatorAgent"
      case AgentType.CutSectorAncillaryAgent => "agents.CutSectorAncillaryAgent"
      case AgentType.FitterAgent => "agents.FitterAgent"
      case AgentType.TransportWorkerAgent => "agents.TransportWorkerAgent"
      case AgentType.WelderAgent => "agents.WelderAgent"
    }
  }

  private def createAgents(agentType: AgentType): Unit = {
    val quantity = configs.getResourceQuantity(agentType)
    val agentClassName = getAgentClasseName(agentType)
    (0 until quantity).foreach(u => getContainerController().createNewAgent(agentClassName + u, agentClassName, null))
  }

  override def setup(): Unit = {
    //AgentType.values.foreach(u => createAgents(u))

    /*
    addBehaviour(new TickerBehaviour(this,configs.steelSheetCreationInterval) {
      override def onTick(): Unit = ???
    })*/
  }
}
