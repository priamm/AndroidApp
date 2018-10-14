package com.enecuumwallet.androidapp.models.inherited.models

data class RequestForSignature(var data: String?)
data class RequestForSignatureList(var data: List<Transaction>)
data class ResponseSignature(var signature : ModelSignature?)

data class ModelSignature(var id: String, var hash: String, var signature: String, var publicKeyEncoded58: String)
