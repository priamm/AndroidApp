package com.enecuumwallet.androidapp.utils

import com.enecuumwallet.androidapp.models.inherited.models.KBlockStructure
import com.google.common.io.BaseEncoding
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ByteBufferUtils{
    fun toByteArray(numero: KBlockStructure): ByteArray {
        val bb = ByteBuffer.allocate(256);
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.put(numero.type)
        bb.putInt(numero.number)
        bb.putInt(numero.time)
        bb.putInt(numero.nonce)
        bb.put(decode64(numero.prev_hash))
        bb.put(decode64(numero.solver))
        val position = bb.position()
        val out = mutableListOf<Byte>()
        for (index: Int in 0..(position - 1)) {
            out.add(bb.get(index))
        }
        return out.toByteArray()
    }

    fun encode64(src: String): String =
            BaseEncoding.base64().encode(src.toByteArray())

    fun encode64(src: ByteArray): String =
            BaseEncoding.base64().encode(src)

    fun decode64(messages1: String): ByteArray {
        return BaseEncoding.base64().decode(messages1)
    }
}