package com.enecuumwallet.androidapp.utils

import timber.log.Timber
import java.math.BigInteger
import java.util.*

class Shoup {

    internal fun F(k: List<BigInteger>, x: BigInteger, p: BigInteger): BigInteger {
        var r = bi(0)
        var d = bi(0)

        for (e in k) {
            r = r.add(x.modPow(d, p).multiply(e).mod(p)).mod(p)
            d = d.add(bi(1))
        }

        return r
    }

    public fun shoup() {
        val p_ = bi(191)
        Timber.d("p_ = " + p_.toString())
        val q_ = bi(113)
        Timber.d("q_ = ", q_)

        val p = p_.multiply(bi(2)).add(bi(1))
        Timber.d("p = ", p)
        val q = q_.multiply(bi(2)).add(bi(1))
        Timber.d("q = ", q)

        val N = p.multiply(q)
        Timber.d("N = ", N)

        val phi = p.subtract(bi(1)).multiply(q.subtract(bi(1)))
        Timber.d("phi = ", phi.toString())

        val lambda = phi.divide(bi(4))
        Timber.d("lambda = ", lambda.toString())

        val e = bi(79)
        Timber.d("e = ", e.toString())

        val d = e.modInverse(lambda)
        Timber.d("d = ", d.toString())

        val delta = bi(120).mod(lambda)

        val Dprime = delta.modInverse(lambda)

        val polynomial = Arrays.asList(d, bi(123), bi(245))
        Timber.d("F(x) = ", polynomial)

        val ids = Arrays.asList(
                bi(0),
                bi(1),
                bi(2),
                bi(3),
                bi(4),
                bi(5),
                bi(6))


        val shares = ArrayList<BigInteger>()

        for (id in ids) {
            shares.add(F(polynomial, id, lambda).multiply(Dprime).mod(lambda))
        }

        Timber.d("shares = ", shares)

        val m = bi(111)

        val l = ArrayList<BigInteger>()

        for (i in 0..2) {
            l.add(delta)
        }

        for (i in 0..2) {
            for (j in 0..2) {
                if (i != j) {
                    val divide = l[i].multiply(bi(1).negate().multiply(bi(j))).divide(bi((i - j)))
                    l.add(i, divide)
                }
            }

        }


        Timber.d("l = ", l)

        val ss = ArrayList<BigInteger>()
        for (i in 0..2) {
            ss.add(m.modPow(shares[i].multiply(bi(2)), N))
        }

        Timber.d("ss = ", ss)

        var sign = bi(1)
        for (i in 0..2) {
            val signa: BigInteger
            val bigInteger = ss[i]
            val bigInteger1 = l[i]
            if (bigInteger1.compareTo(bi(0)) > 0) {
                signa = bigInteger.modPow(bigInteger1.multiply(bi(2)), N)
            } else {
                signa = bigInteger.modInverse(N).modPow(bigInteger1.multiply(bi(-2)), N)
            }
            Timber.d("signa = ", signa.toString())
            sign = sign.multiply(signa).mod(N)
        }

        val gcd1 = ExtendedEuclid.gcd(79.toBigInteger(), 4.toBigInteger())
        val gcd = ExtendedEuclid.gcd(e, sign)
        Timber.d("sign = ", sign.toString())

    }

    fun bi(i: Int): BigInteger {
        return BigInteger.valueOf(i.toLong())
    }


}
