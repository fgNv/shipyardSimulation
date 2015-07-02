package agents

import domain.AgentType
import domain.AgentType.AgentType
import events.EventType
import events.EventType.EventType
import object_graph.CompositionRoot

/**
 * Created by Felipe on 19/06/2015.
 */
class WelderAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.WelderAgent
  }

  private val configuration = CompositionRoot.configurationDataFactory.getConfigurationData()

  override def activeEvent: EventType = EventType.WelderActive

  override def idleEvent: EventType = EventType.WelderIdle

  override def createdEvent: EventType = EventType.WelderCreated

  private def addBeginPartialWeldingBehaviour(): Unit = {
    MessageModule.receiveCondition(this,
      (m) => m != null && m.getContent().contains("CallingForPartialWelding|"),
      (m) => {
        val currentlyWorkingOnId = MessageModule.getProductId(m)
        changeToWorking()
        AgentsModule.addWakerBehaviour(this, configuration.weldingPartialBlockTime, () => {
          changeToIdle()
          MessageModule.send(this, m.getSender.getLocalName, "DonePartialWelding|" + currentlyWorkingOnId)
        })
      })
  }

  override def setup(): Unit = {
    super.setup()

    addBeginPartialWeldingBehaviour()
  }
}
