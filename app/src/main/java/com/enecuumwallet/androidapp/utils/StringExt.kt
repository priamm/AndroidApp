package com.enecuumwallet.androidapp.utils

fun String.hexStringToByteArray(): ByteArray {
    return org.apache.commons.codec.binary.Hex.decodeHex(this.toCharArray())
}

fun ByteArray.toHex(): String {
    return String(org.apache.commons.codec.binary.Hex.encodeHex(this))
}