package agents

import domain.AgentType
import domain.product.SteelSheet
import events.{EventDispatcher, EventType}
import jade.core.Agent
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 25/06/2015.
 */
class DeliveryAgent extends Agent {

  val configs = CompositionRoot.configurationDataFactory.getConfigurationData()

  var steelSheets = new ListBuffer[SteelSheet]

  private def addSteelSheetCreationBehaviour() = {
    AgentsModule.addTickerBehaviour(this, configs.steelSheetCreationInterval, () => {
      steelSheets.append(new SteelSheet())
      EventDispatcher.Dispatch(EventType.SteelSheetCreated)
    })
  }

  private def addSendSteelSheetToTransportWorkerBehaviour() = {
    AgentsModule.addCyclicBehaviour(this, () => {
      val idleTransportAgent = AgentsModule.getIdleAgent(AgentType.TransportWorkerAgent)
      idleTransportAgent match {
        case Some(transportAgent) => MessageModule.send(this, transportAgent, "newSteelSheet")
        case None => ()
      }
    })
  }

  private def addSteelSheetDeliveredConfirmationBehaviour() = {
    MessageModule.receive(this, "steelSheetDelivered", msg => {
      if (steelSheets.length > 0) {
        steelSheets.remove(0)
        EventDispatcher.Dispatch(EventType.SteelSheetTransported)
      }
    })
  }

  override def setup(): Unit = {
    super.setup()
    addSteelSheetCreationBehaviour()
    addSendSteelSheetToTransportWorkerBehaviour()
    addSteelSheetDeliveredConfirmationBehaviour()
  }
}
