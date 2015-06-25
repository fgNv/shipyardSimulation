package object_graph

import agents.AbstractAgent
import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
object CompositionRoot {

  val agents = new ListBuffer[AbstractAgent]
  val configurationDataFactory = ConfigurationDataFactory
  val timer = new infrastructure.Timer()

}


