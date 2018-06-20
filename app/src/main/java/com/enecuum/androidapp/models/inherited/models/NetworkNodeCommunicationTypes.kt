package com.enecuum.androidapp.models.inherited.models

import java.math.BigInteger

data class BasePoAMessage(val type: String)
data class ConnectRequest(val tag: String = Tags.Request.name,
                          val type: String = CommunicationSubjects.Connects.name)

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

//{"tag":"Response","transaction":{"time":40003.379301859,"public_key":210576718018067624383380546555546727660745677996553622007287955533001812694701,"start_balance":30},"type":"Transaction"}
data class TransactionResponse(val tag: String = Tags.Response.name,
                               val type: String = CommunicationSubjects.Transaction.name,
                               val transaction: TransactionIn)

data class MicroblockResponse(val tag: String = Tags.Msg.name,
                              val type: String = CommunicationSubjects.Microblock.name,
                              val microblock: Microblock)

data class KeyblockResponse(val tag: String = Tags.Response.name,
                            val type: String = CommunicationSubjects.Keyblock.name,
                            val keyblock: Keyblock)

data class Microblock(val msg: MicroblockMsg,
                      val sign: MicroblockSignature)

data class Keyblock(val body: String)

data class TransactionOut(val from: String, val to: String, val amount: Int, val uuid: String)

data class TransactionIn(val time: Double, val public_key: BigInteger, val start_balance: BigInteger)

data class MicroblockMsg(val K_hash: String,
                         val wallets: List<Int> = listOf(),
                         val Tx: List<TransactionOut> = listOf(),
                         val i: Int = 0)

data class MicroblockSignature(val sign_r: Int, val sign_s: Int);

