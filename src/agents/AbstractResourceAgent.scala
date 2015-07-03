package agents

import domain.AgentType.AgentType
import domain.ResourceState.ResourceState
import domain.{ActivityLogItem, ResourceState}
import events.EventDispatcher
import events.EventType.EventType
import jade.core.Agent
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
abstract class AbstractResourceAgent extends Agent() {
  var logEntries = new ListBuffer[ActivityLogItem]

  val configurationData = CompositionRoot.configurationDataFactory.getConfigurationData()

  def getResourceState(): ResourceState = {
    logEntries.last.state
  }

  def activeEvent: EventType

  def idleEvent: EventType

  def createdEvent: EventType

  def changeToWorking() = {
    logEntries.append(new ActivityLogItem(CompositionRoot.timer.getCurrentTime(), ResourceState.Working))
    EventDispatcher.Dispatch(activeEvent)
  }

  def changeToIdle() = {
    logEntries.append(new ActivityLogItem(CompositionRoot.timer.getCurrentTime(), ResourceState.Idle))
    EventDispatcher.Dispatch(idleEvent)
  }

  override def setup() {
    logEntries.append(new ActivityLogItem(0, ResourceState.Idle))
    AgentsModule.agents.append(this)
    EventDispatcher.Dispatch(createdEvent)

  }

  def getAgentType(): AgentType
}
