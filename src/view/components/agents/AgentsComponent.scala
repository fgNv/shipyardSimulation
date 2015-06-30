package view.components

import javafx.fxml.FXMLLoader
import javafx.scene.layout.VBox

import events.EventType
import events.EventType.EventType
import view.components.agents.AgentsComponentController

/**
 * Created by Felipe on 27/06/2015.
 */
abstract class IdleAgentsComponent extends VBox {

  val root = new FXMLLoader(getClass().getResource("/view/components/agents/agents_component.fxml"))
  root.setRoot(this)

  def createdEventType: EventType

  def idleEventType: EventType

  def activeEventType: EventType

  def idleTitleText: String

  def activeTitleText : String

  var controller = new AgentsComponentController(idleTitleText,activeTitleText, createdEventType, idleEventType, activeEventType)
  root.setController(controller)
  root.load()
}

class IdleCNCOperatorComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.CNCOperatorCreated

  override def idleEventType = EventType.CNCOperatorIdle

  override def activeEventType = EventType.CNCOperatorActive

  override def idleTitleText = "Operadores de CNC ociosos"

  override def activeTitleText = "Operadores de CNC trabalhando"
}

class IdleCutSectorAncillaryComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.CutSectorAncillaryCreated

  override def idleEventType = EventType.CutSectorAncillaryIdle

  override def activeEventType = EventType.CutSectorAncillaryActive

  override def idleTitleText = "Auxiliares do setor de corte ociosos"
  override def activeTitleText = "Auxiliares do setor de corte trabalhado"
}

class IdleCNCCutMachineComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.CNCCutMachineCreated

  override def idleEventType = EventType.CNCCutMachineIdle

  override def activeEventType = EventType.CNCCutMachineActive

  override def idleTitleText = "Maquinas de corte ociosas"
  override def activeTitleText = "Maquinas de corte trabalhando"
}

class IdleFitterComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.FitterCreated

  override def idleEventType = EventType.FitterIdle

  override def activeEventType = EventType.FitterActive

  override def idleTitleText = "Montadores ociosos"
  override def activeTitleText = "Montadores trabalhando"
}

class IdleWelderComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.WelderCreated

  override def idleEventType = EventType.WelderIdle

  override def activeEventType = EventType.WelderActive

  override def idleTitleText = "Soldadores ociosos"
  override def activeTitleText = "Soldadores trabalhando"
}

class IdleTransportWorkerComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.TransportWorkerCreated

  override def idleEventType = EventType.TransportWorkerIdle

  override def activeEventType = EventType.TransportWorkerActive

  override def idleTitleText = "Trabalhadores do transporte ociosos"
  override def activeTitleText = "Trabalhadores do transporte trabalhando"
}