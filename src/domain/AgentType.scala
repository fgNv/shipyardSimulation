package domain

import agents.AbstractResourceAgent

/**
 * Created by Felipe on 24/06/2015.
 */
object AgentType extends Enumeration  {
  type AgentType = Value
  val TransportWorkerAgent, CNCOperatorAgent, CutSectorAncillaryAgent, CNCCutMachineAgent, FitterAgent, WelderAgent = Value

  def getIdleAgents(allAgents : List[AbstractResourceAgent], quantity : Int, agentType : AgentType ): List[AbstractResourceAgent] ={
    val result = allAgents.filter(p => p.getAgentType() == agentType && p.getResourceState() == ResourceState.Idle)
                          .take(quantity)

    if(result.length == quantity)
        return result

    return List()
  }

}
