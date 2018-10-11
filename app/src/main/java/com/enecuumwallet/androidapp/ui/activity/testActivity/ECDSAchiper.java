package com.enecuumwallet.androidapp.ui.activity.testActivity;

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
}
