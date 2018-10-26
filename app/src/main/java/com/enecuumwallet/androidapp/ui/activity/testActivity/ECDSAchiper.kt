package com.enecuumwallet.androidapp.ui.activity.testActivity

import android.util.Base64
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Hex
import org.bouncycastle.util.encoders.HexEncoder
import java.math.BigInteger
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec

class ECDSAchiper {

    val ecdsaKeyPair: KeyPair
        @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class)
        get() {

            val ecGenSpec = ECGenParameterSpec("secp256k1")

            val g = KeyPairGenerator.getInstance("ECDSA", org.bouncycastle.jce.provider.BouncyCastleProvider())
            g.initialize(ecGenSpec, SecureRandom())

            return g.generateKeyPair()
        }


    companion object {

        val ecdsaSign = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider())

        private fun signData(data: ByteArray) : ByteArray {
            val privateKey = getPrivateKey()
            ecdsaSign.initSign(privateKey)
            ecdsaSign.update(data)
            return ecdsaSign.sign()
        }

        fun compressPubKey(pubKey: BigInteger): String {
            val pubKeyYPrefix = if (pubKey.testBit(0)) "03" else "02"
            val pubKeyHex = pubKey.toString(16)
            val pubKeyX = pubKeyHex.substring(0, 64)
            return pubKeyYPrefix + pubKeyX
        }

        fun isEven(number: BigInteger): Boolean {
            return number.lowestSetBit != 0
        }

        @Throws(GeneralSecurityException::class, Exception::class)
        fun signDataBase64(data: ByteArray): String {
            return Base64.encodeToString(signData(data), Base64.DEFAULT)
        }

        @Throws(GeneralSecurityException::class, Exception::class)
        fun signDataHex(data: ByteArray): String {
            return Hex.toHexString(signData(data))
        }

        fun getSRformSignature(sign : ByteArray) : Pair<String, String> {
            val sign_s = ECDSAutils.extractS(sign).toString(16)
            val sign_r = ECDSAutils.extractR(sign).toString(16)

            return Pair(sign_s, sign_r)
        }


        fun getPrivateKey() : PrivateKey {
            val factory = KeyFactory.getInstance("ECDSA", BouncyCastleProvider())

            val p8ks = PKCS8EncodedKeySpec(
                    Base64.decode(PersistentStorage.getPrivateKey(), Base64.DEFAULT))

            return factory.generatePrivate(p8ks)

        }
    }
}
