
package com.enecuum.androidapp.websocketNew.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type
import java.util.Locale

enum class MessageType {
    REGISTER, REGISTERED, PING, DATA, PONG, ERROR, CHAT;

    class SerializerDeserializer : JsonDeserializer<MessageType>, JsonSerializer<MessageType> {


        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): MessageType? {

            if (json.isJsonNull) {
                return null
            }
            val primitive = json.asJsonPrimitive
            if (!primitive.isString) {
                throw JsonParseException("Non string type of type")
            }

            val asString = json.asString
            try {
                return MessageType.valueOf(asString.toUpperCase(Locale.US))
            } catch (e: IllegalArgumentException) {
                throw JsonParseException("Unknown request type: $asString")
            }

        }


        override fun serialize(messageType: MessageType?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
            return if (messageType == null) {
                null
            } else JsonPrimitive(messageType.toString().toLowerCase(Locale.US))
        }
    }
}
