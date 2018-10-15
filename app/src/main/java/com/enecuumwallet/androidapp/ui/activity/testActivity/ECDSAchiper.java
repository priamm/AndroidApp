package com.enecuumwallet.androidapp.ui.activity.testActivity;

import android.support.annotation.Nullable;
import android.util.Base64;

import org.bouncycastle.jce.PKCS12Util;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.Contract;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import timber.log.Timber;

public class ECDSAchiper {

    public KeyPair getECDSAKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {

            ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("secp256k1");

            KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());

            g.initialize(ecGenSpec, new SecureRandom());

            return g.generateKeyPair();
    }

    public static String compressPubKey(BigInteger pubKey) {
        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
        String pubKeyHex = pubKey.toString(16);
        String pubKeyX = pubKeyHex.substring(0, 64);
        return pubKeyYPrefix + pubKeyX;
    }

    public static boolean isEven(BigInteger number)  {
           return number.getLowestSetBit() != 0;
  }

    public static byte[] signData(byte[] data, String privateKey)
            throws GeneralSecurityException , Exception{

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());

        String base64privateKey = Base64.encodeToString(privateKey.getBytes(), Base64.DEFAULT);
        base64privateKey = base64privateKey.replace("\n" , "");
        ecdsaSign.initSign(base64ToPrivateKey(base64privateKey));
        ecdsaSign.update(data);

        return ecdsaSign.sign();
    }

    private static PrivateKey base64ToPrivateKey(String encodedKey) throws Exception {
        byte[] decodedKey = Base64.decode(encodedKey, Base64.DEFAULT);
        return bytesToPrivateKey(decodedKey);
    }

    private static PrivateKey bytesToPrivateKey(byte[] pkcs8key) throws GeneralSecurityException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8key);
        KeyFactory factory = KeyFactory.getInstance("ECDSA",  new BouncyCastleProvider());
        return factory.generatePrivate(spec);
    }
}
