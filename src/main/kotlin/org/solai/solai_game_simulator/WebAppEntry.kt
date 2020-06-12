/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.solai.solai_game_simulator

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import org.solai.solai_game_simulator.play_chars.CharactersRetriever
import org.solai.solai_game_simulator.play_chars.ExperimentsCharacterRetriever
import org.solai.solai_game_simulator.play_chars.PlaySolGame
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class WebAppEntry

fun main(args: Array<String>) {
    val parsedArgs = mainBody {
        ArgParser(args).parseInto(::SimulatorArgParser)
    }

    if (parsedArgs.experiment.present) {

//        ExperimentsCharacterRetriever.saveAllExperiemntChars()
        ExperimentsCharacterRetriever.playExperimentChars(
                parsedArgs.experiment.experimentLabel,
                parsedArgs.experiment.pairIndex
        )
    }

    if (parsedArgs.playOffline.present) {
        val charsConfig = parsedArgs.playOffline.charactersId
                .map { CharactersRetriever.fetchDBCharacterConfig(it) }
                .onEach { it ?: throw IllegalArgumentException("No characters found for given ids") }
                .map { it!! }

        PlaySolGame.playOffline(
                characterConfigs = charsConfig,
                controlPlayerIndex = parsedArgs.playOffline.controllingPlayerIndex
        )


    }
    else {
        SpringApplication.run(WebAppEntry::class.java, *args)
    }
}