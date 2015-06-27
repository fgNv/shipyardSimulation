package view

import java.util.ResourceBundle
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.{Initializable, FXML}
import javafx.scene.control.Label
import javafx.{fxml => jfxf}
import java.net.URL

import events.{EventDispatcher, EventType}

/**
 * Created by Felipe on 27/06/2015.
 */
class MainController extends Initializable {

  @jfxf.FXML
  var label_cutMachineIdle : Label = _

  var cutMachineIdle = 0

  override def initialize(url : URL, resourceBundle: ResourceBundle ) : Unit = {
    EventDispatcher.RegisterEventHandler(EventType.CNCCutMachineIdle, () => {
      Platform.runLater(new Runnable {
        override def run(): Unit = {
          cutMachineIdle = cutMachineIdle + 1
          label_cutMachineIdle.setText(cutMachineIdle.toString)
        }
      })
    })
  }

  @FXML
  def handleSubmitButtonAction(event: ActionEvent): Unit = {
    label_cutMachineIdle.setText("Sign in button pressed")
  }
}
