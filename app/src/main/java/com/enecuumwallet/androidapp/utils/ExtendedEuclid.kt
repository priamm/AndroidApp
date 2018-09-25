package com.enecuumwallet.androidapp.utils

import java.math.BigInteger

object ExtendedEuclid {

    //  return array [d, a, b] such that d = gcd(p, q), ap + bq = d
    internal fun gcd(p: BigInteger, q: BigInteger): Array<BigInteger?> {
        if (q.equals(BigInteger.ZERO)) {
            val array = arrayOfNulls<BigInteger>(3)
            array[0] = p;
            array[1] = 1.toBigInteger()
            array[2] = 0.toBigInteger()
            return array
        }


        val vals = gcd(q, p % q)
        val d = vals[0]
        val a = vals[2]
        val b = vals[1]?.minus(p.divide(q).multiply(vals[2]))

        val array = arrayOfNulls<BigInteger>(3)
        array[0] = d;
        array[1] = a
        array[2] = b

        return array
    }
}