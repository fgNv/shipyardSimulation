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

  def getAvailableTransportWorker() : Option[TransportWorkerAgent] = {
    agents.filter(a => a.getAgentType() == AgentType.TransportWorkerAgent)
          .map(a => a.asInstanceOf[TransportWorkerAgent])
          .find(a => !a.hasSteelSheet)
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

  private def getWorkersForActivity[A](agentType: AgentType, neededQuantity: Long): Option[ListBuffer[A]] = {
    val agents = AgentsModule.agents
      .filter(a => a.getAgentType() == agentType && a.getResourceState() == ResourceState.Idle)
      .take(neededQuantity.toInt)
      .map(a => a.asInstanceOf[A])

    if (agents.length == neededQuantity)
      return Some(agents)

    None
  }

  def getFittersForPartialMounting(): Option[ListBuffer[FitterAgent]] = {
    getWorkersForActivity[FitterAgent](AgentType.FitterAgent, configuration.fittersNeededInPartialFitting)
  }

  def getWeldersForPartialMounting(): Option[ListBuffer[WelderAgent]] = {
    getWorkersForActivity[WelderAgent](AgentType.WelderAgent, configuration.weldersNeededInPartialFitting)
  }

  def getCUTMachineWithAvailability() = {
    AgentsModule.agents
      .filter(a => a.getAgentType() == AgentType.CNCCutMachineAgent)
      .map(a => a.asInstanceOf[CNCCutMachineAgent])
      .sortWith(_.getItemsInQueueCount < _.getItemsInQueueCount)
      .find(a => a.hasAvailabilityInQueue)

  }
}