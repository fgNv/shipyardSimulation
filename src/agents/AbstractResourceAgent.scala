package agents

import domain.AgentType.AgentType
import domain.ResourceState.ResourceState
import domain.{ActivityLogItem, AgentType, ResourceState}
import events.{EventDispatcher, EventType}
import jade.core.Agent
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
abstract class AbstractResourceAgent extends Agent() {
  def logEntries = new ListBuffer[ActivityLogItem]

  def getResourceState(): ResourceState = {
    logEntries.last.state
  }

  private def launchCreatedEvent(): Unit = {

    val createdEvent = getAgentType() match {
      case AgentType.CNCCutMachineAgent => EventType.CNCCutMachineCreated
      case AgentType.CNCOperatorAgent => EventType.CNCOperatorCreated
      case AgentType.CutSectorAncillaryAgent => EventType.CutSectorAncillaryCreated
      case AgentType.FitterAgent => EventType.FitterCreated
      case AgentType.TransportWorkerAgent => EventType.TransportWorkerCreated
      case AgentType.WelderAgent => EventType.WelderCreated
    }

    EventDispatcher.Dispatch(createdEvent)
  }

  override def setup() {
    logEntries.append(new ActivityLogItem(0, ResourceState.Idle))
    CompositionRoot.agents.append(this)
    launchCreatedEvent()
  }

  def getAgentType(): AgentType
}
