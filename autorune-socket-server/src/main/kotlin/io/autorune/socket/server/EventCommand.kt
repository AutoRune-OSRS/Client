package io.autorune.socket.server

enum class EventCommand(val commandString: String) {

    INSTANCE_SELECT("INSTANCE_SELECT"),
    INSTANCE_ADD("INSTANCE_ADD"),
    INSTANCE_REMOVE("INSTANCE_REMOVE"),

    ACCOUNT_SELECT("ACCOUNT_SELECT"),
    ACCOUNT_LOGIN("ACCOUNT_LOGIN"),
    ACCOUNT_LOGOUT("ACCOUNT_LOGOUT");

    companion object {

        fun getCommand(commandString: String): EventCommand? {
            return values().find { it.commandString == commandString }
        }

    }

}