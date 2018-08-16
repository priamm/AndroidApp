package com.enecuum.androidapp.websocketNew

import com.google.gson.Gson
import com.google.gson.JsonParseException

import com.appunite.websocket.rx.`object`.ObjectParseException
import com.appunite.websocket.rx.`object`.ObjectSerializer

import java.lang.reflect.Type

class GsonObjectSerializer(private val gson: Gson, private val typeOfT: Type) : ObjectSerializer {

    @Throws(ObjectParseException::class)
    override fun serialize(message: String): Any {
        try {
            return gson.fromJson(message, typeOfT)
        } catch (e: JsonParseException) {
            throw ObjectParseException("Could not parse", e)
        }

    }

    @Throws(ObjectParseException::class)
    override fun serialize(message: ByteArray): Any {
        throw ObjectParseException("Could not parse binary messages")
    }

    @Throws(ObjectParseException::class)
    override fun deserializeBinary(message: Any): ByteArray {
        throw IllegalStateException("Only serialization to string is available")
    }

    @Throws(ObjectParseException::class)
    override fun deserializeString(message: Any): String {
        try {
            return gson.toJson(message)
        } catch (e: JsonParseException) {
            throw ObjectParseException("Could not parse", e)
        }

    }

    override fun isBinary(message: Any): Boolean {
        return false
    }
}
