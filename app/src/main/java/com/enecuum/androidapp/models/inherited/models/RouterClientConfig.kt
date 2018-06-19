package com.enecuum.androidapp.models.inherited.models

import java.math.BigInteger

data class RouterClientConfig(val bootNode: String,
                              val bootNodePort : String,
                              val publicKey: String,
                              val modulus: BigInteger,
                              val exponent: BigInteger)