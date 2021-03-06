package view

import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import javafx.application.Application

/**
 * Created by Felipe on 26/06/2015.
 */
class MainView extends Application {

  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Simulacao de estaleiro")
    val resource = getClass.getResource("main_view.fxml")
    val root: Parent = FXMLLoader.load(resource)

    primaryStage.setScene(new Scene(root, 1200, 500))
    primaryStage.show()
  }
}

