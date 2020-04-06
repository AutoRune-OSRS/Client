package io.autorune.client.instance

enum class GameState(val gameState: Int)
{

    STARTUP(0),
    LOGIN_SCREEN(10),
    LOGIN_AUTHENTICATOR(11),
    LOGGING_IN(20),
    GAME_LOADING(25),
    LOGGED_IN(30),
    CONNECTION_LOST(40),
    WORLD_HOPPING(45)

    ;

    companion object
    {

        fun getStatus(gameState: Int): GameState?
        {
            return values().find { it.gameState == gameState }
        }

    }

}