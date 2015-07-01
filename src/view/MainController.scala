package view

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label

import events.EventType

/**
 * Created by Felipe on 27/06/2015.
 */
class MainController extends Initializable {

  @FXML
  var label_steelSheetsDelivered : Label = _

  @FXML
  var label_steelSheetsWaitingTransport : Label = _

  @FXML
  var label_steelSheetsInCNCQueue : Label = _

  private var steelSheetsCreated = 0

  private var steelSheetsInCNCQueue = 0

  private var steelSheetsWaitingTransport = 0

  override def initialize(url : URL, resourceBundle: ResourceBundle ) : Unit = {

    ViewModule.addEventHandler(EventType.SteelSheetCreated, () => {
      steelSheetsCreated = steelSheetsCreated + 1
      steelSheetsWaitingTransport = steelSheetsWaitingTransport + 1
      label_steelSheetsDelivered.setText(steelSheetsCreated.toString)
      label_steelSheetsWaitingTransport.setText(steelSheetsWaitingTransport.toString)
    })

    ViewModule.addEventHandler(EventType.SteelSheetTransported, () => {
      steelSheetsWaitingTransport = steelSheetsWaitingTransport - 1
      label_steelSheetsWaitingTransport.setText(steelSheetsWaitingTransport.toString)
    })

    ViewModule.addEventHandler(EventType.SteelSheetAddedToCNCQueue,()=>{
      steelSheetsInCNCQueue = steelSheetsInCNCQueue + 1
      label_steelSheetsInCNCQueue.setText(steelSheetsInCNCQueue.toString)
    })

    ViewModule.addEventHandler(EventType.SteelSheetMovedFromCNCQueueToProcess,()=>{
      steelSheetsInCNCQueue = steelSheetsInCNCQueue - 1
      label_steelSheetsInCNCQueue.setText(steelSheetsInCNCQueue.toString)
    })
  }
}
