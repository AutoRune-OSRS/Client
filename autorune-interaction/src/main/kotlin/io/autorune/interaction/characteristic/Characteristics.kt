package io.autorune.interaction.characteristic

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.JsonAdapter
import io.autorune.interaction.characteristic.type.Dropping
import io.autorune.utilities.preferences.SystemPreferences
import io.autorune.utilities.random.nextDouble
import java.io.File
import java.io.FileWriter
import java.security.SecureRandom

@JsonAdapter(CharacteristicsAdapter::class)
class Characteristics {

    var viewportMisClickProbability: Double = 0.0

    var widgetMisClickProbability: Double = 0.0

    lateinit var droppingType: Dropping

    var droppingSloppiness: Double = 0.0

    var preferMiniMapWalking: Boolean = false

    var mouseDpi: Int = 400

    var mouseAcceleration: Boolean = false

    var mousePollingRate: Int = 500


    fun save(accountIdentifier: String) {

        val file = getCharacteristicsFile(accountIdentifier)
        val writer = FileWriter(file)

        val jsonString = Gson().toJson(this)

        writer.write(jsonString)
        writer.flush()
        writer.close()

    }


    companion object {

        fun loadCharacteristics(accountIdentifier: String) : Characteristics {

            val file = getCharacteristicsFile(accountIdentifier)

            return if (file.exists()) {
                val jsonObject = JsonParser.parseString(file.readText()).asJsonObject
                val characteristics = Characteristics()
                characteristics.viewportMisClickProbability = jsonObject["viewportMisClickProbability"].asDouble
                characteristics.widgetMisClickProbability = jsonObject["widgetMisClickProbability"].asDouble
                characteristics.droppingType = Dropping.valueOf(jsonObject["droppingType"].asString)
                characteristics.droppingSloppiness = jsonObject["droppingSloppiness"].asDouble
                characteristics.preferMiniMapWalking = jsonObject["preferMiniMapWalking"].asBoolean
                characteristics.mouseDpi = jsonObject["mouseDpi"].asInt
                characteristics.mouseAcceleration = jsonObject["mouseAcceleration"].asBoolean
                characteristics.mousePollingRate = jsonObject["mousePollingRate"].asInt
                characteristics
            } else
                generateCharacteristics(accountIdentifier)

        }

        private fun generateCharacteristics(accountIdentifier: String) : Characteristics {

            val characteristics = Characteristics()

            val random = SecureRandom()

            val dpi = random.nextDouble(400.0, 2800.0)

            val errorModifier = dpi / 80000.0

            characteristics.mouseDpi = dpi.toInt()
            characteristics.mouseAcceleration = random.nextBoolean()
            characteristics.mousePollingRate = random.nextDouble(100.0, 1000.0).toInt()

            characteristics.viewportMisClickProbability = random.nextDouble(0.01+errorModifier, 0.1+errorModifier) // 1/100 to 1/10
            characteristics.widgetMisClickProbability = random.nextDouble(0.005+errorModifier, 0.05+errorModifier) // 1/200 to 1/20

            characteristics.droppingType = Dropping.values()[random.nextInt(Dropping.values().size)]
            characteristics.droppingSloppiness = random.nextDouble(0.04+errorModifier, 0.20+errorModifier) // 1/25 to 1/5

            characteristics.preferMiniMapWalking = random.nextBoolean()

            characteristics.save(accountIdentifier)

            return characteristics

        }

        private fun getCharacteristicsFile(accountIdentifier: String): File {

            val characteristicDir = SystemPreferences.getCharacteristicDirectory()

            val filePath = characteristicDir.resolve("$accountIdentifier.json")

            return filePath.toFile()

        }

    }

}