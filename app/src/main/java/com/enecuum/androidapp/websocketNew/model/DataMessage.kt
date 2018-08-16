
package com.enecuum.androidapp.websocketNew.model

class DataMessage(private val id: String, private val message: String) : Message(MessageType.DATA) {

    fun message(): String {
        return message
    }

    fun id(): String {
        return id
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is DataMessage) return false
        if (!super.equals(o)) return false

        val that = o as DataMessage?

        return if (id != that!!.id) false else message == that.message

    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }

    override fun toString(): String {
        return "DataResponse{" +
                "id='" + id + '\''.toString() +
                ", message='" + message + '\''.toString() +
                "} " + super.toString()
    }
}
