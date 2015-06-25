package agents

import domain.AgentType.AgentType
import domain.ResourceState.ResourceState
import domain.{ActivityLogItem, ResourceState}
import jade.core.Agent
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
abstract class AbstractAgent extends Agent() {
  def logEntries = new ListBuffer[ActivityLogItem]

  def getResourceState() : ResourceState ={
    logEntries.last.state
  }

  override def setup() {
    logEntries.append(new ActivityLogItem(0, ResourceState.Idle))
    CompositionRoot.agents.append(this)
  }

  def getAgentType(): AgentType
}
