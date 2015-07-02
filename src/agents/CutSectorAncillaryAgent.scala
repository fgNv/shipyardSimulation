package agents

import domain.AgentType
import domain.AgentType._
import events.{EventDispatcher, EventType}
import events.EventType.EventType
import object_graph.CompositionRoot

/**
 * Created by Felipe on 19/06/2015.
 */
class CutSectorAncillaryAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CutSectorAncillaryAgent
  }

  private val configurationData = CompositionRoot.configurationDataFactory.getConfigurationData()

  override def activeEvent: EventType = EventType.CutSectorAncillaryActive

  override def idleEvent: EventType = EventType.CutSectorAncillaryIdle

  override def createdEvent: EventType = EventType.CutSectorAncillaryCreated

  private def addMoveSheetToCNCBehaviour(): Unit = {
    MessageModule.receive(this, "newSheetToQueue", (m) => {
      changeToWorking()
      AgentsModule.addWakerBehaviour(this, configurationData.timeForMaterialMovingInCNC, () => {
        changeToIdle()
        EventDispatcher.Dispatch(EventType.SheetPlacedInCNC)
        MessageModule.send(this, m.getSender.getLocalName, "sheetPlacedInCNC")
      })
    })
  }

  private def addMovePiecesFromCNCBehaviour(): Unit = {
    MessageModule.receive(this, "movePiecesFromCNC", (m) => {
      changeToWorking()
      AgentsModule.addWakerBehaviour(this, configurationData.timeForMaterialMovingInCNC * configurationData.partsFromSheet, () => {
        changeToIdle()
        EventDispatcher.Dispatch(EventType.PartsMovedFromCNC)
        MessageModule.send(this, m.getSender.getLocalName, "partsMovedFromCNC")
      })
    })
  }

  override def setup(): Unit = {
    super.setup()
    addMoveSheetToCNCBehaviour
    addMovePiecesFromCNCBehaviour
  }
}
