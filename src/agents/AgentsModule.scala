package agents

import domain.{ResourceState, AgentType}
import domain.AgentType.AgentType
import jade.core.Agent
import jade.core.behaviours.{TickerBehaviour, CyclicBehaviour}

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 28/06/2015.
 */
object AgentsModule {

  var agents = new ListBuffer[AbstractResourceAgent]

  def getIdleAgent(agentType: AgentType): Option[AbstractResourceAgent] = {
    agents.find(a => a.getAgentType() == agentType && a.getResourceState() == ResourceState.Idle)
  }

  def addCyclicBehaviour(agent: Agent, behaviourAction: () => Unit): Unit = {
    agent.addBehaviour(new CyclicBehaviour() {
      override def action(): Unit = {
        behaviourAction()
      }
    })
  }

  def addTickerBehaviour(agent: Agent, interval: Long, behaviourAction: () => Unit): Unit = {
    agent.addBehaviour(new TickerBehaviour(agent, interval) {
      override def onTick(): Unit = {
        behaviourAction()
      }
    })
  }

  def getCUTMachineWithAvailability() = {
    AgentsModule.agents
      .filter(a => a.getAgentType() == AgentType.CNCCutMachineAgent)
      .map(a => a.asInstanceOf[CNCCutMachineAgent])
      .find(a => a.hasAvailabilityInQueue)
  }
}
