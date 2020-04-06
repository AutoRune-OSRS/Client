package io.autorune.interaction.characteristic.type

enum class Dropping {

    COLUMN_SINGLE, //Down first, up second, down third, up fourth
    COLUMN_DOUBLE, //Down first and second back and forth, and up third and fourth back and forth
    COLUMN_SPIRAL, //Around the outside starting south

    ROW_SINGLE,
    ROW_DOUBLE,
    ROW_SPIRAL,

    OBNOXIOUS


}