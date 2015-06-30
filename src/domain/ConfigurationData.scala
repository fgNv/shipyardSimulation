package domain

import domain.AgentType.AgentType

/**
 * Created by Felipe on 19/06/2015.
 */
class ConfigurationData(_transportTimeToCNC: Long,
                        _cutTime: Double,
                        _partsFromSheet: Double,
                        _timeForMaterialMovingInCNC: Double,
                        _cncQueueCapacity: Double,
                        _steelSheetCreationInterval: Long,
                        _partsForPartialBlock: Double,
                        _partialBlocksForBlock: Double,
                        _blocksForHull: Double,
                        _fittingPartialBlockTime: Double,
                        _weldingPartialBlockTime: Double,
                        _fittingBlockTime: Double,
                        _weldingBlockTime: Double,
                        _fittingHullTime: Double,
                        _weldingHullTime: Double,
                        _fittersNeededInPartialFitting: Double,
                        _weldersNeededInPartialFitting: Double,
                        _fittersNeededInFitting: Double,
                        _weldersNeededInFitting: Double,
                        _fittersNeededInEdification: Double,
                        _weldersNeededInEdification: Double,
                        _transportWorkerAgentQuantity : Int,
                        _cNCOperatorAgentQuantity : Int,
                        _cutSectorAncillaryAgentQuantity : Int,
                        _cNCCutMachineAgentQuantity : Int,
                        _fitterAgentQuantity : Int,
                        _welderAgentQuantity : Int) {

  def getResourceQuantity(agentType: AgentType) : Int = {
    agentType match {
      case AgentType.CNCCutMachineAgent => _cNCCutMachineAgentQuantity
      case AgentType.CNCOperatorAgent => _cNCOperatorAgentQuantity
      case AgentType.CutSectorAncillaryAgent => _cutSectorAncillaryAgentQuantity
      case AgentType.FitterAgent => _fitterAgentQuantity
      case AgentType.TransportWorkerAgent => _transportWorkerAgentQuantity
      case AgentType.WelderAgent => _welderAgentQuantity
    }
  }

  def transportTimeToCNC = _transportTimeToCNC

  def cutTime = _cutTime

  def partsFromSheet = _partsFromSheet

  def timeForMaterialMovingInCNC = _timeForMaterialMovingInCNC

  def cncQueueCapacity = _cncQueueCapacity

  def steelSheetCreationInterval = _steelSheetCreationInterval

  def partsForPartialBlock = _partsForPartialBlock

  def partialBlocksForBlock = _partialBlocksForBlock

  def blocksForHull = _blocksForHull

  def fittingPartialBlockTime = _fittingPartialBlockTime

  def weldingPartialBlockTime = _weldingPartialBlockTime

  def fittingBlockTime = _fittingBlockTime

  def weldingBlockTime = _weldingBlockTime

  def fittingHullTime = _fittingHullTime

  def weldingHullTime = _weldingHullTime

  def fittersNeededInPartialFitting = _fittersNeededInPartialFitting

  def weldersNeededInPartialFitting = _weldersNeededInPartialFitting

  def fittersNeededInFitting = _fittersNeededInFitting

  def weldersNeededInFitting = _weldersNeededInFitting

  def fittersNeededInEdification = _fittersNeededInEdification

  def weldersNeededInEdification = _weldersNeededInEdification
}
