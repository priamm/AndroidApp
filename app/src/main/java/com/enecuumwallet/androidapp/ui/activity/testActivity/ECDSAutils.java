package com.enecuumwallet.androidapp.ui.activity.testActivity;

import java.math.BigInteger;
import java.util.Arrays;

public class ECDSAutils {

    public static BigInteger extractR(byte[] signature) throws Exception {
        int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
        int lengthR = signature[startR + 1];
        return new BigInteger(Arrays.copyOfRange(signature, startR + 2, startR + 2 + lengthR));
    }

    public static BigInteger extractS(byte[] signature) throws Exception {
        int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
        int lengthR = signature[startR + 1];
        int startS = startR + 2 + lengthR;
        int lengthS = signature[startS + 1];
        return new BigInteger(Arrays.copyOfRange(signature, startS + 2, startS + 2 + lengthS));
    }
}
