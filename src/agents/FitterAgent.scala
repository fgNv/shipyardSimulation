package agents

import domain.AgentType
import domain.AgentType.AgentType
import events.EventType
import events.EventType.EventType
import object_graph.CompositionRoot

/**
 * Created by Felipe on 19/06/2015.
 */
class FitterAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.FitterAgent
  }

  private val configuration = CompositionRoot.configurationDataFactory.getConfigurationData()

  override def activeEvent: EventType = EventType.FitterActive

  override def idleEvent: EventType = EventType.FitterIdle

  override def createdEvent: EventType = EventType.FitterCreated

  private def addBeginPartialFittingBehaviour() : Unit = {
    MessageModule.receive(this,"CallingForPartialFitting",(m) =>{
      changeToWorking()
      Thread.sleep(configuration.fittingPartialBlockTime)
      changeToIdle()
    })
  }

  override def setup(): Unit = {
    super.setup()

    addBeginPartialFittingBehaviour()
  }
}
