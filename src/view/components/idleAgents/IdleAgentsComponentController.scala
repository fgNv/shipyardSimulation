package view.components.idleAgents

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label

import events.EventType._
import view.ViewModule

/**
 * Created by Felipe on 27/06/2015.
 */
class IdleAgentsComponentController(titleText : String, createdEventType : EventType, idleEventType : EventType, activeEventType : EventType)
  extends Initializable{

  @FXML
  var label_idleAgentsCounter: Label = _
  @FXML
  var label_idleAgentsTitle: Label = _

  private var idleAgentsCounter = 0

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

    label_idleAgentsTitle.setText(titleText)
    ViewModule.addEventHandler(createdEventType, () => {
      idleAgentsCounter = idleAgentsCounter + 1
      label_idleAgentsCounter.setText(idleAgentsCounter.toString)
    })

    ViewModule.addEventHandler(idleEventType, () => {
      idleAgentsCounter = idleAgentsCounter + 1
      label_idleAgentsCounter.setText(idleAgentsCounter.toString)
    })

    ViewModule.addEventHandler(activeEventType, () => {
      idleAgentsCounter = idleAgentsCounter - 1
      label_idleAgentsCounter.setText(idleAgentsCounter.toString)
    })
  }
}
