package io.autorune.socket.server

enum class Event(val eventName: String) {

    ACCOUNT_CONTROL("ACCOUNT_CONTROL"),
    INSTANCE_CONTROL("INSTANCE_CONTROL"),
    INITIAL_SYNC("INITIAL_SYNC"),
    SCREENSHOT("SCREENSHOT");

}