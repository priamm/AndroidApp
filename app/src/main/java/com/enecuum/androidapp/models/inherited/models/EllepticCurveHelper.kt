package com.enecuum.androidapp.models.inherited.models

import com.google.crypto.tink.BinaryKeysetWriter
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.signature.SignatureKeyTemplates
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException

object EcdsaKeyPairGenerator {
    private val SIGNATURE_KEY_TEMPLATE = SignatureKeyTemplates.ECDSA_P256

    @Throws(GeneralSecurityException::class, IOException::class)
    public fun generateEcdsaKeyPair(): Pair<ByteArrayOutputStream, ByteArrayOutputStream> {
        val privatekeyFileOutputStream = ByteArrayOutputStream()
        val publickeyFileOutputStream = ByteArrayOutputStream()
        val privateKeyHandle = KeysetHandle.generateNew(SIGNATURE_KEY_TEMPLATE)
        CleartextKeysetHandle.write(privateKeyHandle, BinaryKeysetWriter.withOutputStream(privatekeyFileOutputStream))
        val publicKeyHandle = privateKeyHandle.getPublicKeysetHandle()
        CleartextKeysetHandle.write(publicKeyHandle, BinaryKeysetWriter.withOutputStream(publickeyFileOutputStream))
        return Pair(privatekeyFileOutputStream, publickeyFileOutputStream)
    }
}