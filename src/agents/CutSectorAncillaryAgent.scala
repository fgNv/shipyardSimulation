package agents

import domain.AgentType
import domain.AgentType._
import events.EventType.EventType
import events.{EventDispatcher, EventType}

/**
 * Created by Felipe on 19/06/2015.
 */
class CutSectorAncillaryAgent extends AbstractResourceAgent() {
  override def getAgentType(): AgentType = {
    AgentType.CutSectorAncillaryAgent
  }

  override def activeEvent: EventType = EventType.CutSectorAncillaryActive

  override def idleEvent: EventType = EventType.CutSectorAncillaryIdle

  override def createdEvent: EventType = EventType.CutSectorAncillaryCreated

  private def addMoveSheetToCNCBehaviour(): Unit = {
    MessageModule.receive(this, "moveSteelSheetToCNC", (m) => {
      changeToWorking()
      doWait(configurationData.timeForMaterialMovingInCNC)
      changeToIdle()
      EventDispatcher.Dispatch(EventType.SheetPlacedInCNC)
      MessageModule.send(this, m.getSender.getLocalName, "sheetPlacedInCNC")
    })
  }

  private def addMovePiecesFromCNCBehaviour(): Unit = {
    MessageModule.receive(this, "movePiecesFromCNC", (m) => {
      changeToWorking()
      doWait(configurationData.timeForMaterialMovingInCNC * configurationData.partsFromSheet)
      changeToIdle()
      EventDispatcher.Dispatch(EventType.PartsMovedFromCNC)
      MessageModule.send(this, m.getSender.getLocalName, "partsMovedFromCNC")
    })
  }

  override def setup(): Unit = {
    super.setup()
    addMoveSheetToCNCBehaviour
    addMovePiecesFromCNCBehaviour
  }
}
