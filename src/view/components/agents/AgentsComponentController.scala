package view.components.agents

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label

import events.EventType._
import view.ViewModule

/**
 * Created by Felipe on 27/06/2015.
 */
class AgentsComponentController(idleTitleText: String, workingTitleText: String,
                                createdEventType: EventType, idleEventType: EventType,
                                activeEventType: EventType)
  extends Initializable {

  @FXML
  var label_idleAgentsCounter: Label = _
  @FXML
  var label_idleAgentsTitle: Label = _

  @FXML
  var label_workingAgentsTitle: Label = _
  @FXML
  var label_workingAgentsCounter: Label = _

  private var idleAgentsCounter = 0
  private var workingAgentsCounter = 0

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    label_idleAgentsTitle.setText(idleTitleText)
    label_workingAgentsTitle.setText(workingTitleText)
    ViewModule.addEventHandler(createdEventType, () => {
      idleAgentsCounter = idleAgentsCounter + 1
      label_idleAgentsCounter.setText(idleAgentsCounter.toString)
      label_workingAgentsCounter.setText(workingAgentsCounter.toString)
    })

    ViewModule.addEventHandler(idleEventType, () => {
      idleAgentsCounter = idleAgentsCounter + 1
      workingAgentsCounter = workingAgentsCounter - 1
      label_idleAgentsCounter.setText(idleAgentsCounter.toString)
      label_workingAgentsCounter.setText(workingAgentsCounter.toString)
    })

    ViewModule.addEventHandler(activeEventType, () => {
      idleAgentsCounter = idleAgentsCounter - 1
      workingAgentsCounter = workingAgentsCounter + 1
      label_idleAgentsCounter.setText(idleAgentsCounter.toString)
      label_workingAgentsCounter.setText(workingAgentsCounter.toString)
    })
  }
}
