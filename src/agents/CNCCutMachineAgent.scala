package agents

import domain.AgentType
import domain.AgentType.AgentType
import domain.product.SteelSheet
import events.EventType
import events.EventType.EventType
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCCutMachineAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCCutMachineAgent
  }

  var steelSheetQueue = new ListBuffer[SteelSheet]
  val maxQueueCapacity = CompositionRoot.configurationDataFactory.getConfigurationData().cncQueueCapacity

  def hasAvailabilityInQueue = {
    steelSheetQueue.length < maxQueueCapacity
  }

  override def activeEvent: EventType = EventType.CNCCutMachineActive

  override def idleEvent: EventType = EventType.CNCCutMachineIdle

  override def createdEvent: EventType = EventType.CNCCutMachineCreated
}
