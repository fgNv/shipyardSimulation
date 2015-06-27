package object_graph

import agents.AbstractResourceAgent
import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 19/06/2015.
 */
object CompositionRoot {

  val agents = new ListBuffer[AbstractResourceAgent]
  val configurationDataFactory = ConfigurationDataFactory
  val timer = new infrastructure.Timer()

}


