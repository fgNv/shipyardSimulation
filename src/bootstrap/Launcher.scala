package bootstrap

import javafx.application.Application

import domain.AgentModule
import object_graph.CompositionRoot
import view.MainView

/**
 * Created by Felipe on 19/06/2015.
 */
object Launcher {

  def initTask(task : () => Unit) : Unit ={
    val runnable = new Runnable {
      override def run(): Unit = {
        task()
      }
    }
    val thread = new Thread(runnable)
    thread.start()
  }

  def main(args: Array[String]) {

    val agents = (AgentModule.buildAgentsCreationString(CompositionRoot.configurationDataFactory.getConfigurationData())
                  + "deliveryAgent:agents.DeliveryAgent")
    val newArgs = (args
      :+ "-gui"
      :+ agents)

    initTask(() => Application.launch(classOf[MainView], args: _*))
    initTask(() => jade.Boot.main(newArgs))
  }
}
