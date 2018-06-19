package com.enecuum.androidapp.models.inherited.models

import org.spongycastle.jce.ECNamedCurveTable
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class EllepticCurveHelper {
    fun generatePublicAndPrivate() {
        val ecSpec = ECNamedCurveTable.getParameterSpec("prime192v1")
        val g = KeyPairGenerator.getInstance("ECDSA", "BC")
        g.initialize(ecSpec, SecureRandom())
        val pair = g.generateKeyPair()
        val fact = KeyFactory.getInstance("ECDSA", "BC");
        val public = fact.generatePublic(X509EncodedKeySpec(pair.getPublic().getEncoded()));
        val private = fact.generatePrivate(PKCS8EncodedKeySpec(pair.getPrivate().getEncoded()));
        Pair(public, private)
    }
}