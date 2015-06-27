package agents

import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import object_graph.CompositionRoot

/**
 * Created by Felipe on 25/06/2015.
 */
class DeliveryAgent extends Agent {

  val configs = CompositionRoot.configurationDataFactory.getConfigurationData()

  override def setup(): Unit = {
    super.setup()

    addBehaviour(new TickerBehaviour(this,configs.steelSheetCreationInterval) {
      override def onTick(): Unit = {

      }
    })
  }
}
