package domain.product

import domain.{ResourceState, ActivityLogItem}
import object_graph.CompositionRoot

import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 24/06/2015.
 */
abstract class AbstractProduct {
  def logEntries = new ListBuffer[ActivityLogItem]

  logEntries.append(new ActivityLogItem(CompositionRoot.timer.getCurrentTime(), ResourceState.Idle))
}
