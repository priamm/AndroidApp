
package com.enecuum.androidapp.websocketNew.model

class PingMessage(private val message: String) : Message(MessageType.PING) {

    fun message(): String {
        return message
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is PingMessage) return false
        if (!super.equals(o)) return false

        val that = o as PingMessage?

        return message == that!!.message

    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }

    override fun toString(): String {
        return "PingMessage{" +
                "message='" + message + '\''.toString() +
                "} " + super.toString()
    }
}
