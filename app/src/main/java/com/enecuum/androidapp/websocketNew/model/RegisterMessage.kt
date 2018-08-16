
package com.enecuum.androidapp.websocketNew.model

import com.google.gson.annotations.SerializedName

class RegisterMessage(@field:SerializedName("auth_token")
                      val authToken: String) : Message(MessageType.REGISTER) {

    fun authToken(): String {
        return authToken
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RegisterMessage) return false

        val that = o as RegisterMessage?

        return authToken == that!!.authToken

    }

    override fun hashCode(): Int {
        return authToken.hashCode()
    }

    override fun toString(): String {
        return "RegisterRequest{" +
                "message='" + authToken + '\''.toString() +
                "} " + super.toString()
    }
}
