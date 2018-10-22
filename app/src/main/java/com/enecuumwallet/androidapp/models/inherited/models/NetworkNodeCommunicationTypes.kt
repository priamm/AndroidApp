package com.enecuumwallet.androidapp.models.inherited.models

import android.os.Parcel
import android.os.Parcelable

data class BasePoAMessage(val type: String)

data class GetApiServices(val type: String = CommunicationSubjects.getApiServers.name)

data class ConnectBNRequest(val tag: String = Tags.Request.name,
                            val type: String = CommunicationSubjects.PotentialConnects.name)

data class ConnectBNResponse(val tag: String = Tags.Response.name,
                             val type: String = CommunicationSubjects.PotentialConnects.name,
                             val connects: List<ConnectPointDescription>)

data class ConnectApiServicesResponse(val type: String = CommunicationSubjects.getApiServers.name,
                                      val msg : List<ConnectPointDescription>)

//{"tag":"Response","type":"ErrorOfConnect", "Msg":"{\"tag\":\"Request\",\"type\":\"PotentialConnects\"}", "comment" : "not a connect msg"}
data class ErrorResponse(val tag: String = Tags.Response.name,
                         val type: String = CommunicationSubjects.ErrorOfConnect.name,
                         val comment: String,
                         val Msg: String)

data class ConnectPointDescription(val ip: String,
                                   val port: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ip)
        parcel.writeString(port)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConnectPointDescription> {
        override fun createFromParcel(parcel: Parcel): ConnectPointDescription {
            return ConnectPointDescription(parcel)
        }

        override fun newArray(size: Int): Array<ConnectPointDescription?> {
            return arrayOfNulls(size)
        }
    }
}

data class PoANodeUUIDResponse(val tag: String = Tags.Response.name,
                               val type: String = CommunicationSubjects.NodeId.name,
                               val nodeId: String,
                               val version: String,
                               val nodeType: String = NodeTypes.PoA.name)

data class ReconnectResponse(val tag: String = Tags.Response.name,
                             val type: String = CommunicationSubjects.NodeId.name,
                             val node_id: String)

data class ReconnectAction(val tag: String = Tags.Action.name,
                           val type: String = CommunicationSubjects.Connect.name,
                           val node_type: String = "PoA")


data class ReceivedBroadcastMessage(val tag: String = Tags.Msg.name,
                                    val type: String = CommunicationSubjects.Broadcast.name,
                                    val node_type: String = "All",
                                    val msg: String,
                                    val from: String)

data class ReceivedBroadcastKeyblockMessage(val tag: String = Tags.Msg.name,
                                            val type: String = CommunicationSubjects.KeyBlock.name,
                                            val keyBlock: Keyblock)

data class PowsResponse(val tag: String = Tags.Response.name,
                        val type: String = CommunicationSubjects.PoWList.name,
                        val poWList: List<String>)

data class AddressedMessageRequest(val tag: String? = Tags.Msg.name,
                                   val type: String? = CommunicationSubjects.MsgTo.name,
                                   val from: String?,
                                   val to: String?,
                                   val msg: String?)

data class AddressedMessageRequestWithTransactionSignature(
                                   val tag: String? = Tags.transactions.name,
                                   val type: String? = CommunicationSubjects.response.name,
                                   val to: String?,
                                   val sign: ModelSignature,
                                   val version: String? = null)


data class AddressedMessageRequestWithSignature(
                                   val tag: String? = Tags.transactions.name,
                                   val type: String? = CommunicationSubjects.response.name,
                                   val to: String?,
                                   val sign : ModelSignature?)


data class AddressedMessageRequestWithTransactions(
                                   val tag: String? = Tags.transactions.name,
                                   val type: String? = CommunicationSubjects.request.name,
                                   val from: String?,
                                   val tx : List<Transaction>,
                                   val version : String)

data class ErrorEvent(
        val tag: String? = Tags.error.name,
        val code : String)

data class AddressedMessageResponse(val tag : String = Tags.Msg.name,
                                    val type : String = CommunicationSubjects.MsgTo.name,
                                    val from : String,
                                    val msg : String)


data class TeamResponse(val tag: String = Tags.Response.name,
                        val type: String = CommunicationSubjects.Team.name,
                        val data: List<String>)

data class TransactionRequest(val tag: String = Tags.Request.name,
                              val type: String = CommunicationSubjects.Transactions.name,
                              val number: Int = 3)

data class TransactionResponse(val tag: String = Tags.Response.name,
                               val type: String = CommunicationSubjects.Transactions.name,
                               val transactions: List<Transaction> = emptyList())


data class MicroblockResponse(val tag: String = Tags.Msg.name,
                              val type: String = CommunicationSubjects.Microblock.name,
                              val microblock: Microblock)

data class Microblock(val msg: MicroblockMsg,
                      val sign: MicroblockSignature)

data class Keyblock(val body: String, val verb: String)


data class Transaction(
        val owner: String,
        val receiver: String,
        val amount: Int,
        val currency: String = "ENQ",
        val timestamp: Long,
        val sign: MicroblockSignature,
        val uuid: Int)

data class MicroblockMsg(val K_hash: String,
                         val wallets: List<String> = listOf(),
                         val Tx: List<Transaction> = listOf(),
                         val publisher: String = "QYy3AT4a3Z88MpEoGDixRgxtWW8v3RfSbJLFQEyFZwMe",
                         val sign: MicroblockSignature = MicroblockSignature())

data class MicroblockSignature(val sign_r: String = "NDU=", val sign_s: String = "NDU=");

data class KBlockStructure(val time: Int, val nonce: Int, val number: Int, val type: Byte, val prev_hash: String, val solver: String)

data class ResponseRpc(val jsonrpc: String, val result: Result?, val id: Int)

data class ResponseStringRpc(val jsonrpc: String, val result: String?, val id: Int)

data class Result(val balance: Int)

data class MsgSignature(var signature : ModelSignature?)