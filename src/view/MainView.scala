package view

import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import javafx.application.Application

/**
 * Created by Felipe on 26/06/2015.
 */
class MainView extends Application {
  println("Test()")

  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Simulação de estaleiro!")
    val resource = getClass.getResource("main_view.fxml")
    val root: Parent = FXMLLoader.load(resource)

    primaryStage.setScene(new Scene(root, 300, 300))
    primaryStage.show()
  }
}

