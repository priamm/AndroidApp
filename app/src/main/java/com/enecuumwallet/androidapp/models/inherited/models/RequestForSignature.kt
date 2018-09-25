package com.enecuumwallet.androidapp.models.inherited.models

data class RequestForSignature(var data: String?)
data class RequestForSignatureList(var data: List<Transaction>)
data class ResponseSignature(var signature : Signature?)

data class Signature(var id: String, var hash: String, var signature: String, var publicKeyEncoded58: String)