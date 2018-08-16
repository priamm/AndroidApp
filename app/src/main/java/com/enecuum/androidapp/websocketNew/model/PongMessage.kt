
package com.enecuum.androidapp.websocketNew.model

class PongMessage(private val response: String) : Message(MessageType.PONG) {

    fun response(): String {
        return response
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is PongMessage) return false
        if (!super.equals(o)) return false

        val that = o as PongMessage?

        return response == that!!.response

    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + response.hashCode()
        return result
    }

    override fun toString(): String {
        return "PongResponse{" +
                "message='" + response + '\''.toString() +
                "} " + super.toString()
    }
}
