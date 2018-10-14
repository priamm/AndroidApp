package com.enecuumwallet.androidapp.ui.activity.testActivity;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECGenParameterSpec;

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

    public static String compressPubKey2(BigInteger pubKey) {
        String pubKeyYPrefix = pubKey.getLowestSetBit() != 0 ? "02" : "03";
        String pubKeyHex = pubKey.toString(16);
        String pubKeyX = pubKeyHex.substring(0, 64);
        return pubKeyYPrefix + pubKeyX;
    }

    public static boolean isEven(BigInteger number)  {
           return number.getLowestSetBit() != 0;
  }
}
