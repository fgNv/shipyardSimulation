package object_graph

import domain.ConfigurationData

/**
 * Created by Felipe on 22/06/2015.
 */
object ConfigurationDataFactory {
  def getConfigurationData() = {
    val transportTimeToCNC = 5000
    val cutTime = 10
    val partsFromSheet = 20
    val timeForMaterialMovingInCNC = 50
    val cncQueueCapacity = 15
    val steelSheetCreationInterval = 1000
    val partsForPartialBlock = 5
    val partialBlocksForBlock = 5
    val blocksForHull = 5
    val fittingPartialBlockTime = 5
    val weldingPartialBlockTime = 6
    val fittingBlockTime = 5
    val weldingBlockTime = 5
    val fittingHullTime = 5
    val weldingHullTime = 5
    val fittersNeededInPartialFitting = 5
    val weldersNeededInPartialFitting = 5
    val fittersNeededInFitting = 5
    val weldersNeededInFitting = 5
    val fittersNeededInEdification = 5
    val weldersNeededInEdification = 5

    val transportWorkerAgentQuantity = 3
    val cNCOperatorAgentQuantity = 6
    val cutSectorAncillaryAgentQuantity = 6
    val cNCCutMachineAgentQuantity = 6
    val fitterAgentQuantity = 6
    val welderAgentQuantity = 6

    new ConfigurationData(transportTimeToCNC,
      cutTime,
      partsFromSheet,
      timeForMaterialMovingInCNC,
      cncQueueCapacity,
      steelSheetCreationInterval,
      partsForPartialBlock,
      partialBlocksForBlock,
      blocksForHull,
      fittingPartialBlockTime,
      weldingPartialBlockTime,
      fittingBlockTime,
      weldingBlockTime,
      fittingHullTime,
      weldingHullTime,
      fittersNeededInPartialFitting,
      weldersNeededInPartialFitting,
      fittersNeededInFitting,
      weldersNeededInFitting,
      fittersNeededInEdification,
      weldersNeededInEdification,
      transportWorkerAgentQuantity,
      cNCOperatorAgentQuantity,
      cutSectorAncillaryAgentQuantity,
      cNCCutMachineAgentQuantity,
      fitterAgentQuantity,
      welderAgentQuantity)
  }
}
