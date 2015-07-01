package agents

import domain.AgentType
import domain.product.{ProcessedPart, SteelSheet}
import events.{EventDispatcher, EventType}
import jade.core.Agent
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 25/06/2015.
 */
class DeliveryAgent extends Agent {

  val configs = CompositionRoot.configurationDataFactory.getConfigurationData()

  private val steelSheets = new ListBuffer[SteelSheet]
  private val parts = new ListBuffer[ProcessedPart]

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

  private def addReceivePiecesDoneNotificationBehaviour(): Unit = {
    MessageModule.receive(this, "partsFromSheetProduced", (m) => {
      (0 until configs.partsFromSheet).foreach(u => {
        EventDispatcher.Dispatch(EventType.PartProduced)
        parts.append(new ProcessedPart)
      })
    })
  }

  private def addDispatchPartialMountingBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (parts.length >= configs.partsForPartialBlock) {
        val agents = AgentsModule.getFittersForPartialMounting()
        agents match {
          case Some(fitters) => fitters.foreach(a => MessageModule.send(this, a, "CallingForPartialFitting"))
          case None => ()
        }
      }
    })
  }

  override def setup(): Unit = {
    super.setup()
    addSteelSheetCreationBehaviour()
    addSendSteelSheetToTransportWorkerBehaviour()
    addSteelSheetDeliveredConfirmationBehaviour()
    addReceivePiecesDoneNotificationBehaviour()
    addDispatchPartialMountingBehaviour()
  }
}
