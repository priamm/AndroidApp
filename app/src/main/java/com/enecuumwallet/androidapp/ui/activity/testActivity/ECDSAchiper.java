package com.enecuumwallet.androidapp.ui.activity.testActivity;

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
import java.security.spec.PKCS8EncodedKeySpec;

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

    public static byte[] signData(byte[] data, byte[] privateKey)
            throws GeneralSecurityException {
        Signature signer = Signature.getInstance("SHA256WithRSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        signer.initSign(keyToValue(privateKey));
        signer.update(data);
        return signer.sign();
    }

    private static PrivateKey keyToValue(byte[] pkcs8key) throws GeneralSecurityException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8key);
        KeyFactory factory = KeyFactory.getInstance("ECDSA");
        return factory.generatePrivate(spec);
    }
}
