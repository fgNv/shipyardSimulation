package events

/**
 * Created by Felipe on 24/06/2015.
 */
object EventType extends Enumeration  {
  type EventType = Value
  val SteelSheetCreated,
  SteelSheetTransported,
  SteelSheetProcessed,
  TransportWorkerIdle,
  PiecesMovedFromCNC,
  SteelSheetAddedToCNCQueue,
  SteelSheetMovedFromCNCQueueToProcess,
  PartProduced,
  TransportWorkerActive,
  CNCOperatorIdle,
  CutSectorAncillaryIdle,
  CNCCutMachineIdle,
  FitterIdle,
  WelderIdle,
  CNCOperatorActive,
  CutSectorAncillaryActive,
  CNCCutMachineActive,
  FitterActive,
  WelderActive ,
  CNCOperatorCreated,
  CutSectorAncillaryCreated,
  CNCCutMachineCreated,
  FitterCreated,
  TransportWorkerCreated,
  SheetPlacedInCNC,
  PartsMovedFromCNC,
  WelderCreated= Value
}
