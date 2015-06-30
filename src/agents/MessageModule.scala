package agents

import jade.core.behaviours.CyclicBehaviour
import jade.core.{AID, Agent}
import jade.lang.acl.ACLMessage

/**
 * Created by Felipe on 28/06/2015.
 */
object MessageModule {
  def send(sender: Agent, receiver: Agent, content: String): Unit = {
    send(sender, receiver.getLocalName, content)
  }

  def send(sender: Agent, receiverLocalName: String, content: String): Unit = {
    val newSheetMsg = new ACLMessage(ACLMessage.INFORM)
    newSheetMsg.addReceiver(new AID(receiverLocalName, AID.ISLOCALNAME))
    newSheetMsg.setLanguage("English")
    newSheetMsg.setOntology("Event")
    newSheetMsg.setContent(content)
    sender.send(newSheetMsg)
  }

  def receive(receiver: Agent, behaviour: CyclicBehaviour, messageHandling: ACLMessage => Unit) = {
    val msg = receiver.receive()
    if (msg != null) {
      messageHandling(msg)
    }
    behaviour.block()
  }
}
