package com.example.jukeboxapp.model

import androidx.datastore.core.Serializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.InputStream
import java.io.OutputStream


@Serializable
data class JukeboxState(
    val isBluetoothConnected: Boolean = false, // Determines if you can move on to the PairedScreen and Remote Screen
    val lastSongSelection: String = "00",
    val machineName: String = "My Jukebox",
    val machineType: String = "N/A",
    val isPairedToMachine: Boolean = false // If its paired, the machine info will show up on the main page, regardless
                                          // if you are connected to the bluetooth or not
)

object JukeboxStateSerializer : Serializer<JukeboxState> {
    private val json = Json {
        serializersModule = SerializersModule {
            contextual(JukeboxState::class, JukeboxState.serializer())
        }
    }

    override val defaultValue: JukeboxState
        get() = JukeboxState()

    private val deserializationStrategy: DeserializationStrategy<JukeboxState> = JukeboxState.serializer()
    private val serializationStrategy: SerializationStrategy<JukeboxState> = JukeboxState.serializer()

    override suspend fun readFrom(input: InputStream): JukeboxState {
        return json.decodeFromString(deserializationStrategy, input.readBytes().decodeToString())
    }

    override suspend fun writeTo(t: JukeboxState, output: OutputStream) {
        output.write(json.encodeToString(serializationStrategy, t).encodeToByteArray())
    }
}
