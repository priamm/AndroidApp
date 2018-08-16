
package com.enecuum.androidapp.websocketNew.model

class ErrorMessage(private val response: String) : Message(MessageType.ERROR) {

    fun response(): String {
        return response
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ErrorMessage) return false
        if (!super.equals(o)) return false

        val that = o as ErrorMessage?

        return response == that!!.response

    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + response.hashCode()
        return result
    }

    override fun toString(): String {
        return "ErrorResponse{" +
                "message='" + response + '\''.toString() +
                "} " + super.toString()
    }
}
