package com.enecuum.androidapp.models.inherited.models

data class BasePoAMessage(val type: String)
data class ConnectRequest(val tag: String = Tags.Request.name,
                          val type: String = CommunicationSubjects.Connects.name)

data class ErrorResponse(val tag: String = Tags.Response.name,
                         val type: String = CommunicationSubjects.Error.name,
                         val reason: String,
                         val Msg: String)

data class ConnectResponse(val tag: String = Tags.Response.name,
                           val type: String = CommunicationSubjects.Connects.name,
                           val connects: List<ConnectPointDescription>)

data class ConnectPointDescription(val ip: String,
                                   val port: String)

data class PoANodeUUIDRequest(val tag: String = Tags.Request.name,
                              val type: String = CommunicationSubjects.NodeId.name)

data class PoANodeUUIDResponse(val tag: String = Tags.Response.name,
                               val type: String = CommunicationSubjects.NodeId.name,
                               val nodeId: String,
                               val nodeType: String = NodeTypes.PoA.name)

data class ReconnectNotification(val tag: String = Tags.Msg.name,
                                 val type: String = CommunicationSubjects.Connect.name,
                                 val ip: String,
                                 val port: String)

data class BroadcastPoAMessage(val tag: String = Tags.Request.name,
                               val type: String = CommunicationSubjects.Broadcast.name,
                               val recipientType: String = NodeTypes.PoA.name,
                               val msg: String)

data class ReceivedBroadcastMessage(val tag: String = Tags.Msg.name,
                                    val type: String = CommunicationSubjects.Broadcast.name,
                                    val msg: String,
                                    val idFrom: String)

data class ReceivedBroadcastKeyblockMessage(val tag: String = Tags.Msg.name,
                                            val type: String = CommunicationSubjects.Broadcast.name,
                                            val msg: Keyblock,
                                            val idFrom: String)

data class PowsRequest(val tag: String = Tags.Request.name,
                       val type: String = CommunicationSubjects.PoWList.name)

data class PowsResponse(val tag: String = Tags.Response.name,
                        val type: String = CommunicationSubjects.PoWList.name,
                        val poWList: List<String>)

data class AddressedMessageRequest(val tag: String = Tags.Msg.name,
                                   val type: String = CommunicationSubjects.MsgTo.name,
                                   val destination: String,
                                   val msg: String)

data class AddressedMessageResponse(val tag: String = Tags.Msg.name,
                                    val type: String = CommunicationSubjects.MsgTo.name,
                                    val sender: String,
                                    val msg: String)


data class TransactionRequest(val tag: String = Tags.Request.name,
                              val type: String = CommunicationSubjects.Transaction.name,
                              val number: Int)

data class TransactionResponse(val tag: String = Tags.Response.name,
                               val type: String = CommunicationSubjects.Transaction.name,
                               val transaction: Transaction)

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
                         val Tx: List<Transaction> = listOf())

data class MicroblockSignature(val sign_r: String = "NDU=", val sign_s: String = "NDU=");

