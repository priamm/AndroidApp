
package com.enecuum.androidapp.websocketNew.model

class ChatMessage(val message: String, val from: String) : Message(MessageType.CHAT) {

    fun message(): String {
        return message
    }

    fun from(): String {
        return from
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ChatMessage) return false

        val that = o as ChatMessage?

        return message == that!!.message && from == that.from

    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + from.hashCode()
        return result
    }

    override fun toString(): String {
        return "RegisterRequest{" +
                "message='" + message + '\''.toString() +
                "from='" + from + '\''.toString() +
                "} " + super.toString()
    }
}
