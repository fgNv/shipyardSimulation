package agents

import domain.product.SteelSheet
import domain.{AgentType, ResourceState}
import events.{EventType, EventDispatcher}
import jade.core.Agent
import jade.core.behaviours.{CyclicBehaviour, TickerBehaviour}
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 25/06/2015.
 */
class DeliveryAgent extends Agent {

  val configs = CompositionRoot.configurationDataFactory.getConfigurationData()

  private val steelSheets = new ListBuffer[SteelSheet]

  private def addSteelSheetCreationBehaviour() = {
    addBehaviour(new TickerBehaviour(this, configs.steelSheetCreationInterval) {
      override def onTick(): Unit = {
        steelSheets.append(new SteelSheet())
        EventDispatcher.Dispatch(EventType.SteelSheetCreated)
      }
    })
  }

  private def addSendSteelSheetToTransportWorkerBehaviour() = {
    addBehaviour(new CyclicBehaviour(this) {
      override def action(): Unit = {
        if (steelSheets.length == 0)
          return

        val idleTransportWorker = (u: AbstractResourceAgent) =>
          u.getResourceState() == ResourceState.Idle && u.getAgentType() == AgentType.TransportWorkerAgent

        val idleTransportAgent = AgentsModule.agents.find(idleTransportWorker)
        idleTransportAgent match {
          case Some(transportAgent) => MessageModule.send(myAgent, transportAgent, "newSteelSheet")
          case None => return
        }
      }
    })
  }

  private def addSteelSheetDeliveredConfirmationBehaviour() = {
    addBehaviour(new CyclicBehaviour(this) {
      override def action(): Unit = {
        MessageModule.receive(myAgent, this, msg => {
          val content = msg.getContent()
          if (content == "steelSheetDelivered" && steelSheets.length > 0) {
            steelSheets.remove(0)
            EventDispatcher.Dispatch(EventType.SteelSheetTransported)
          }
        })
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
