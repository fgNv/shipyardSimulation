package view.components

import javafx.fxml.FXMLLoader
import javafx.scene.layout.VBox

import events.EventType
import events.EventType.EventType
import view.components.idleAgents.IdleAgentsComponentController

/**
 * Created by Felipe on 27/06/2015.
 */
abstract class IdleAgentsComponent extends VBox {

  val root = new FXMLLoader(getClass().getResource("/view/components/idleAgents/idle_agents_component.fxml"))
  root.setRoot(this)

  def createdEventType: EventType

  def idleEventType: EventType

  def activeEventType: EventType

  def titleText: String

  var controller = new IdleAgentsComponentController(titleText, createdEventType, idleEventType, activeEventType)
  root.setController(controller)
  root.load()
}

class IdleCNCOperatorComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.CNCOperatorCreated

  override def idleEventType = EventType.CNCOperatorIdle

  override def activeEventType = EventType.CNCOperatorActive

  override def titleText = "Operadores de CNC ociosos"
}

class IdleCutSectorAncillaryComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.CutSectorAncillaryCreated

  override def idleEventType = EventType.CutSectorAncillaryIdle

  override def activeEventType = EventType.CutSectorAncillaryActive

  override def titleText = "Auxiliares do setor de corte ociosos"
}

class IdleCNCCutMachineComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.CNCCutMachineCreated

  override def idleEventType = EventType.CNCCutMachineIdle

  override def activeEventType = EventType.CNCCutMachineActive

  override def titleText = "Maquinas de corte ociosas"
}

class IdleFitterComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.FitterCreated

  override def idleEventType = EventType.FitterIdle

  override def activeEventType = EventType.FitterActive

  override def titleText = "Montadores ociosos"
}

class IdleWelderComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.WelderCreated

  override def idleEventType = EventType.WelderIdle

  override def activeEventType = EventType.WelderActive

  override def titleText = "Soldadores ociosos"
}

class IdleTransportWorkerComponent extends IdleAgentsComponent {
  override def createdEventType = EventType.TransportWorkerCreated

  override def idleEventType = EventType.TransportWorkerIdle

  override def activeEventType = EventType.TransportWorkerActive

  override def titleText = "Trabalhadores do transporte ociosos"
}