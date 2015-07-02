package agents

import domain.{ResourceState, AgentType}
import domain.AgentType.AgentType
import jade.core.Agent
import jade.core.behaviours.{WakerBehaviour, TickerBehaviour, CyclicBehaviour}
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 28/06/2015.
 */
object AgentsModule {

  var agents = new ListBuffer[AbstractResourceAgent]

  val configuration = CompositionRoot.configurationDataFactory.getConfigurationData()

  def getIdleAgent(agentType: AgentType): Option[AbstractResourceAgent] = {
    agents.find(a => a.getAgentType() == agentType && a.getResourceState() == ResourceState.Idle)
  }

  def addCyclicBehaviour(agent: Agent, behaviourAction: (Agent) => Unit): Unit = {
    agent.addBehaviour(new CyclicBehaviour() {
      override def action(): Unit = {
        behaviourAction(agent)
      }
    })
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

  def addWakerBehaviour(agent: Agent, interval: Long, behaviourAction: () => Unit): Unit = {
    agent.addBehaviour(new WakerBehaviour(agent, interval) {
      override def onWake(): Unit = {
        behaviourAction()
      }
    })
  }

  def getFittersForPartialMounting(): Option[ListBuffer[FitterAgent]] = {
    val agents = AgentsModule.agents
      .filter(a => a.getAgentType() == AgentType.FitterAgent && a.getResourceState() == ResourceState.Idle)
      .take(configuration.fittersNeededInPartialFitting)
      .map(a => asInstanceOf[FitterAgent])

    if (agents.length == configuration.weldersNeededInPartialFitting)
      return Some(agents)

    None
  }

  def getWeldersForPartialMounting(): Option[ListBuffer[WelderAgent]] = {
    val agents = AgentsModule.agents
      .filter(a => a.getAgentType() == AgentType.WelderAgent && a.getResourceState() == ResourceState.Idle)
      .take(configuration.weldersNeededInPartialFitting)
      .map(a => asInstanceOf[WelderAgent])

    if (agents.length == configuration.weldersNeededInPartialFitting)
      return Some(agents)

    None
  }

  def getCUTMachineWithAvailability() = {
    AgentsModule.agents
      .filter(a => a.getAgentType() == AgentType.CNCCutMachineAgent)
      .map(a => a.asInstanceOf[CNCCutMachineAgent])
      .find(a => a.hasAvailabilityInQueue)
  }
}