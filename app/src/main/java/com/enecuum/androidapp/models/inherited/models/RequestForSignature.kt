package com.enecuum.androidapp.models.inherited.models

data class RequestForSignature(var data: String?)
data class ResponseSignature(var signature: Signature?)

data class Signature(var id: String, var hash: String, var signature: String, var publicKeyEncoded58: String)
