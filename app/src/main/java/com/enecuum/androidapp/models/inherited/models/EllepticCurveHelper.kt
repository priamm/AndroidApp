package com.enecuum.androidapp.models.inherited.models

import com.enecuum.androidapp.application.EnecuumApplication
import com.google.crypto.tink.BinaryKeysetWriter
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.signature.PublicKeySignFactory
import com.google.crypto.tink.signature.PublicKeyVerifyFactory
import com.google.crypto.tink.signature.SignatureKeyTemplates
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException


object EcdsaKeyPairManager {
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

    fun addKey() {
        EnecuumApplication.keysetManager().add(SignatureKeyTemplates.ECDSA_P256)
    }

    public fun sign(src: ByteArray): ByteArray {
        val signer = PublicKeySignFactory.getPrimitive(
                EnecuumApplication.keysetManager().keysetHandle)
        return signer.sign(src)
    }

    @Throws(java.security.GeneralSecurityException::class)
    public fun verify(signature: ByteArray, data: ByteArray) {
        val verifier = PublicKeyVerifyFactory.getPrimitive(
                EnecuumApplication.keysetManager().keysetHandle)
        verifier.verify(signature, data)
    }
}