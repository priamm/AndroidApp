
package com.enecuum.androidapp.websocketNew.model

class RegisteredMessage : Message(MessageType.REGISTERED) {

    override fun toString(): String {
        return "RegisteredResponse{}"
    }

    override fun equals(o: Any?): Boolean {
        return o is RegisteredMessage
    }

    override fun hashCode(): Int {
        return 1
    }
}
