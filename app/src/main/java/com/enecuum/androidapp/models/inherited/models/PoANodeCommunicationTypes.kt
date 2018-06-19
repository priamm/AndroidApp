package com.enecuum.androidapp.models.inherited.models

class PoANodeCommunicationTypes {
    data class NodeAnnounce(val poaid: String,
                            val type: String = PoACommunicationSubjects.Announce.name) //TODO: add Node characteristics from https://github.com/Enecuum/FirebaseIdentity
    data class PoWTailRequest(val type: String = PoACommunicationSubjects.Tail.name)
    data class PoWTailResponse(val type: String = PoACommunicationSubjects.Keyblock.name,
                               val prev_hash: String,
                               val time: String,
                               val number: Int,
                               val nonce: String)
    data class PowPeekRequest(val type: String = PoACommunicationSubjects.Peek.name,
                              val range_min: Int,
                              val range_max: Int)
    data class PoWPeekResponse(val type: String = PoACommunicationSubjects.Peek.name,
                               val data: List<PoWTailResponse>)
}