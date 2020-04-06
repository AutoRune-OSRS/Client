package io.autorune.interaction.characteristic

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class CharacteristicsAdapter : TypeAdapter<Characteristics>() {

    override fun write(writer: JsonWriter, characteristics: Characteristics) {

        writer.beginObject()

        writer.name("viewportMisClickProbability").value(characteristics.viewportMisClickProbability)
        writer.name("widgetMisClickProbability").value(characteristics.widgetMisClickProbability)
        writer.name("droppingType").value(characteristics.droppingType.name)
        writer.name("droppingSloppiness").value(characteristics.droppingSloppiness)
        writer.name("preferMiniMapWalking").value(characteristics.preferMiniMapWalking)
        writer.name("mouseDpi").value(characteristics.mouseDpi)
        writer.name("mouseAcceleration").value(characteristics.mouseAcceleration)
        writer.name("mousePollingRate").value(characteristics.mousePollingRate)

        writer.endObject()

    }

    override fun read(reader: JsonReader): Characteristics {

        return Characteristics()

    }

}