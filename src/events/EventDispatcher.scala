package events

import events.EventType.EventType

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by Felipe on 26/06/2015.
 */
object EventDispatcher {

  private val handlers  = new mutable.HashMap[EventType,ListBuffer[() => Unit]]()

  EventType.values.foreach(u => handlers.put(u,new ListBuffer[() => Unit]))

  def RegisterEventHandler(eventType: EventType, handler: () => Unit): Unit = {
    handlers(eventType).append(handler)
  }

  def Dispatch(eventType: EventType): Unit = {
    println (eventType)
    handlers(eventType).foreach(u => u())
  }
}
