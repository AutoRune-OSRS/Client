package io.autorune.client.instance

import io.autorune.client.AutoRune
import io.autorune.client.event.account.AccountSelectedEvent
import io.autorune.client.instance.account.AccountCredentials
import io.autorune.client.instance.account.AccountProfile
import io.autorune.osrs.api.Client
import io.autorune.script.executor.ScriptExecutor
import io.autorune.script.debug.DebugScript
import kotlinx.coroutines.delay
import java.applet.Applet
import java.awt.image.BufferedImage

class ClientInstance(var applet: Applet, val client: Client, var parameters: Map<String, String>)
{

    var codeBase: String = ""

    val scriptExecutor: ScriptExecutor = ScriptExecutor(client)

    val debugExecutor: ScriptExecutor = ScriptExecutor(client)

    var accountProfile = AccountProfile()

    var instanceId: Int = -1

    var osrsRevision: Int = 0

    var gameStatus = GameState.STARTUP

    init
    {

        debugExecutor.setScript(DebugScript())
        debugExecutor.startScript()

    }

    suspend fun setAccount(email: String, password: String, pin: Int)
    {

        if (gameStatus != GameState.LOGIN_SCREEN)
            logout(false)

        val credentials = AccountCredentials(email, password, pin)

        accountProfile.setCredentials(credentials)

        AutoRune.eventBus.post(AccountSelectedEvent(instanceId, credentials))

    }

    fun getScreenshotImage() : BufferedImage {

        val component = applet

        val image = BufferedImage(component.width, component.height, BufferedImage.TYPE_INT_RGB)

        component.paint(image.graphics)

        return image

    }

    suspend fun login() : Boolean
    {

        if (gameStatus == GameState.LOGIN_SCREEN) {

            val credentials = accountProfile.getCredentials() ?: return false

            client.username = credentials.email
            client.password = credentials.password

            client.promptLoginCredentials(false)

            client.updateLoginPreferenceType(false)

            client.updateGameState(20)

            return true

        }

        return true

    }

    suspend fun logout(force: Boolean)
    {

        val fixed = client.rootWidget == 548

        val openLogoutButton = client.rootWidget shl 16 or (if (fixed) 35 else 30)

        val logoutButton = 182 shl 16 or 8

        val defaultWidgetOption = 57

        if (gameStatus == GameState.LOGGED_IN) {

            while (gameStatus != GameState.LOGIN_SCREEN) {

                if (force)
                    client.logout()
                else {
                    client.sendMenuAction(-1, openLogoutButton, defaultWidgetOption, 1, "Logout", "", 739, 402)
                    client.sendMenuAction(-1, logoutButton, defaultWidgetOption, 1, "Logout", "", 739, 402)
                    delay(1000)
                }
            }

        }

    }

    //todo flags
    suspend fun swapWorldOnLoginScreen(worldId: Int)
    {

        if(gameStatus != GameState.LOGIN_SCREEN) return

        if(client.worlds == null)
        {

            client.fetchWorldList()

            while (client.worldRequest == null || !client.worldRequest.isDone)
                continue

            client.fetchWorldList()

        }

        val world = client.worlds.first { it.id == worldId }

        client.changeWorld(world)

    }

}