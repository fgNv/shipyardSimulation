package view

import javafx.application.Platform

import events.EventDispatcher
import events.EventType._

/**
 * Created by Felipe on 27/06/2015.
 */
object ViewModule {

  def addEventHandler(eventType: EventType, action: () => Unit): Unit = {
    EventDispatcher.RegisterEventHandler(eventType, () => {
      Platform.runLater(new Runnable {
        override def run(): Unit = {
          action()
        }
      })
    })
  }
}
