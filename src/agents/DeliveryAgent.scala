package agents

import domain.AgentType
import domain.product._
import events.{EventDispatcher, EventType}
import jade.core.Agent
import object_graph.CompositionRoot

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 25/06/2015.
 */
class DeliveryAgent extends Agent {

  val configs = CompositionRoot.configurationDataFactory.getConfigurationData()

  private val steelSheets = new ListBuffer[SteelSheet]
  private val parts = new ListBuffer[ProcessedPart]
  private val partialBlocks = new ListBuffer[PartialBlock]
  private val blocks = new ListBuffer[Block]
  private val hulls = new ListBuffer[Hull]

  private var partialBlocksWaitingWelding = 0
  private var blocksWaitingWelding = 0
  private var hullsWaitingWelding = 0

  private var currentPartialBlockFittingId = 0
  private var currentPartialBlockWeldingId = 0
  private var currentBlockFittingId = 0
  private var currentBlockWeldingId = 0

  private val partialFittingWorkControl = new mutable.HashMap[Int, Int]()
  private val partialWeldingWorkControl = new mutable.HashMap[Int, Int]()

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
        case Some(transportAgent) => MessageModule.send(this, transportAgent.getLocalName, "newSteelSheet")
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
    val currentAgent = this
    AgentsModule.addCyclicBehaviour(currentAgent, (ag) => {
      if (parts.length >= configs.partsForPartialBlock) {
        val agents = AgentsModule.getFittersForPartialMounting()
        agents match {
          case Some(fitters) => {
            partialFittingWorkControl.put(currentPartialBlockFittingId, 0)
            val messageContent = "CallingForPartialFitting|" + currentPartialBlockFittingId.toString
            fitters.foreach(a => MessageModule.send(ag, a.getLocalName, messageContent))
            currentPartialBlockFittingId = currentPartialBlockFittingId + 1
          }
          case None => ()
        }
      }
    })
  }

  private def addReceivePartialBlockFittingDoneConfirmation(): Unit = {
    MessageModule.receiveCondition(this,
      (m) => {
        m != null && m.getContent().contains("DonePartialFitting|")
      },
      (m) => {
        val productId = MessageModule.getProductId(m)
        val previousValue = partialFittingWorkControl(productId)
        partialFittingWorkControl.update(productId, previousValue + 1)
      })
  }

  private def addPartialBlockFittingVerificationBehaviour(): Unit = {
    partialFittingWorkControl
      .filter { case (k, v) => v == configs.fittersNeededInPartialFitting }
      .map { case (k, v) => k }
      .foreach((k) => {
      (0 until configs.partsForPartialBlock).foreach(u => parts.remove(0))
      partialFittingWorkControl.remove(k)
      partialBlocksWaitingWelding = partialBlocksWaitingWelding + 1
    })
  }

  private def addDispatchPartialBlockWeldingBehaviour(): Unit = {
    AgentsModule.addCyclicBehaviour(this, () => {
      if (partialBlocksWaitingWelding > 0) {
        val welders = AgentsModule.getWeldersForPartialMounting()

        welders match {
          case Some(agents) => {
            agents.foreach(a => MessageModule.send(this, a.getLocalName, "CallingForPartialWelding|" + currentPartialBlockWeldingId))
            partialBlocksWaitingWelding = partialBlocksWaitingWelding - 1
          }
          case None => ()
        }
      }
    })
  }

  private def addReceivePartialBlockWeldingDoneConfirmation(): Unit = {
    MessageModule.receiveCondition(this,
      (m) => m != null && m.getContent().contains("DonePartialWelding|"),
      (m) => {
        val productId = MessageModule.getProductId(m)
        val previousValue = partialWeldingWorkControl(productId)
        partialWeldingWorkControl.update(productId, previousValue + 1)
      })
  }

  override def setup(): Unit = {
    super.setup()
    addSteelSheetCreationBehaviour()
    addSendSteelSheetToTransportWorkerBehaviour()
    addSteelSheetDeliveredConfirmationBehaviour()
    addReceivePiecesDoneNotificationBehaviour()
    addDispatchPartialMountingBehaviour()
    addReceivePartialBlockFittingDoneConfirmation()
    addPartialBlockFittingVerificationBehaviour()
    addDispatchPartialBlockWeldingBehaviour()
  }
}
