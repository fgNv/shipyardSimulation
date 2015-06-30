package agents

import domain.AgentType

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 28/06/2015.
 */
object AgentsModule {

  var agents = new ListBuffer[AbstractResourceAgent]

  def getCUTMachineWithAvailability = {
    AgentsModule.agents
      .filter(a => a.getAgentType() == AgentType.CNCCutMachineAgent)
      .map(a => a.asInstanceOf[CNCCutMachineAgent])
      .find(a => a.hasAvailabilityInQueue)
  }
}
