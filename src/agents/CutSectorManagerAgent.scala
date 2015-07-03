package agents

import events.{EventType, EventDispatcher}
import jade.core.Agent

/**
 * Created by Felipe on 02/07/2015.
 */
class CutSectorManagerAgent extends Agent {
  private def addSteelSheetDeliveredConfirmationBehaviour() = {
    MessageModule.receive(this, "steelSheetDelivered", msg => {
      EventDispatcher.Dispatch(EventType.SteelSheetTransported)
    })
  }

  override def setup() : Unit = {
    addSteelSheetDeliveredConfirmationBehaviour()
  }
}
