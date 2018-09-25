package com.enecuumwallet.androidapp.models.inherited.models

import java.security.*
import kotlin.experimental.and

object Sha {
    @Throws(NoSuchAlgorithmException::class)
    fun hash256(data: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(data.toByteArray())
        return bytesToHex(md.digest())
    }

    @Throws(NoSuchAlgorithmException::class)
    fun hash256(data: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(data)
        return md.digest()
    }

    fun bytesToHex(bytes: ByteArray): String {
        val result = StringBuffer()
        for (byt in bytes) result.append(Integer.toString((byt and 0xff.toByte()) + 0x100, 16).substring(1))
        return result.toString()
    }
}