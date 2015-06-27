package view

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.control.Label

import events.{EventType, EventDispatcher}

/**
 * Created by Felipe on 26/06/2015.
 */
class MainView extends Application {
  println("Test()")

  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Sup!")

    val root = new StackPane
    val label = new Label("Hello world!")
    root.getChildren.add(label)

    var cutMachineIdle = 0

    EventDispatcher.RegisterEventHandler(EventType.CNCCutMachineIdle,() =>{
      Platform.runLater(new Runnable {
        override def run(): Unit = {
          cutMachineIdle = cutMachineIdle + 1
          label.setText(cutMachineIdle.toString)
        }
      })
    })

    primaryStage.setScene(new Scene(root, 300, 300))
    primaryStage.show()
  }
}

