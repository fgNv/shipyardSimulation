package bootstrap

/**
 * Created by Felipe on 19/06/2015.
 */
object Launcher {
  def main(args : Array[String]){

/*    val agents = ( "TransportWorkerAgent:agents.TransportWorkerAgent;" +
                   "CNCOperatorAgent:agents.CNCOperatorAgent;" +
                   "CutSectorAncillaryAgent:agents.CutSectorAncillaryAgent;" +
                   "CNCCutMachineAgent:agents.CNCCutMachineAgent;" +
                   "FitterAgent:agents.FitterAgent;" +
                   "WelderAgent:agents.WelderAgent")*/

      val newArgs = (args :+ "-gui"
                          :+ "initAgent:agents.InitializerAgent")

      jade.Boot.main(newArgs)
  }
}
