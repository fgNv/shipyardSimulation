package agents

import domain.AgentType
import domain.AgentType._
import events.{EventDispatcher, EventType}
import events.EventType.EventType
import object_graph.CompositionRoot

/**
 * Created by Felipe on 19/06/2015.
 */
class CNCOperatorAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CNCOperatorAgent
  }

  private val configuration = CompositionRoot.configurationDataFactory.getConfigurationData()

  override def activeEvent: EventType = EventType.CNCOperatorActive

  override def idleEvent: EventType = EventType.CNCOperatorIdle

  override def createdEvent: EventType = EventType.CNCOperatorCreated

  private def addProcessSteelSheetBehaviour(): Unit = {
    MessageModule.receive(this, "processSteelSheet", msg => {
      changeToWorking()
      AgentsModule.addWakerBehaviour(this, configuration.cutTime, () => {
        changeToIdle()
        EventDispatcher.Dispatch(EventType.SteelSheetProcessed)
        MessageModule.send(this, msg.getSender.getLocalName, "steelSheetProcessDone")
      })
    })
  }

  override def setup(): Unit = {
    super.setup()
    addProcessSteelSheetBehaviour()
  }
}
