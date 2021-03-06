package org.solai.solai_game_simulator.web

import org.solai.solai_game_simulator.character_queue.GameSimulationResult
import org.solai.solai_game_simulator.metrics.ExistingMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController()
class WebController {

    @Autowired
    lateinit var simulationMeasurer: SimulationMeasureService

    @GetMapping("/hello")
    fun hello() = "hello"

    @GetMapping("/runningSimulations")
    fun runningSimulations(): List<String> {
        return simulationMeasurer.simulator.executor.getExecutionsSimulationIds()
    }

    @GetMapping("/existingMetrics")
    fun existingMetrics(): List<String> = ExistingMetrics.getAllMetricNames()

    @GetMapping("/intermediateResult/{simulationId}")
    fun intermediateResult(@PathVariable simulationId: String): GameSimulationResult? {
        val simMeasure = simulationMeasurer.simulator.executor.getExecutingMeasure(simulationId)
        return simMeasure
                ?.let { simulationMeasurer.simulator.queueExecutor.simulationMeasureToResult(it) }
                ?: run { null }
    }

    @PostMapping("/headlessSimulations")
    fun headlessSimulations(@RequestBody headless: Boolean): Boolean {
        return simulationMeasurer.simulator.simulationFactory.let {
            it.headless = headless
            it.headless
        }
    }

    @PostMapping("/simulationUpdateDelay")
    fun simulationUpdateDelay(@RequestBody updateDelayMillis: Float): Float {
        return simulationMeasurer.simulator.queueExecutor.let {
            it.simulationUpdateDelayMillis = updateDelayMillis
            it.simulationUpdateDelayMillis
        }
    }

//    @DeleteMapping("/removeSimulation")
//    fun removeSimulation(): Boolean {
//
//    }
}