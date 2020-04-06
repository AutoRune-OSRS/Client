package io.autorune.client.instance

import com.google.common.eventbus.Subscribe
import io.autorune.client.AutoRune
import io.autorune.client.applet.ClientAppletStub
import io.autorune.client.event.instance.*
import io.autorune.client.interaction.MouseRecorder
import io.autorune.osrs.api.Client
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.swing.SwingUtilities

object ClientInstanceRepo {

    private val instances = mutableListOf<ClientInstance>()

    var selectedInstance: Int = -1

    @Subscribe fun addInstance(event : AddInstanceEvent)
    {

        GlobalScope.launch {

            val instance = InjectedInstanceFetcher.getInjectedInstance(false)

            instances.add(instance)

            val applet = instance.applet
            applet.setStub(ClientAppletStub(instance))
            applet.isFocusable = true

            instance.client.initCallback()

            SwingUtilities.invokeLater {

                AutoRune.eventBus.post(AddedInstanceEvent(instance))

            }

        }

    }

    @Subscribe fun removeInstance(event : RemoveInstanceEvent)
    {

        val instanceId = event.instanceId

        val instance = instances.first { it.instanceId == instanceId }

        instances.remove(instance)

        instances.filter { inst -> inst.instanceId > instanceId }.forEach {
            instanceToShift -> instanceToShift.instanceId--
        }

        GlobalScope.launch {
            instance.applet.destroy()
        }

        AutoRune.eventBus.post(RemovedInstanceEvent(instanceId))

    }

    @Subscribe fun selectInstance(event : SelectInstanceEvent)
    {

        val oldInstance = instances.find { it.instanceId == event.oldInstanceId }
        val newInstance = instances.find { it.instanceId == event.newInstanceId }

        oldInstance?.debugExecutor?.stopExecutor()
        newInstance?.debugExecutor?.startExecutor()

        selectedInstance = event.newInstanceId

        AutoRune.eventBus.post(SelectedInstanceEvent(event.newInstanceId))

    }

    fun getAllInstances() : List<ClientInstance> {
        return instances
    }

    fun getInstanceFromId(instanceId: Int) : ClientInstance?
    {
        return instances.find { it.instanceId == instanceId }
    }

    /**
     * Dont Delete Please
     */
    @JvmStatic
    fun getInstanceFromClient(client: Client): ClientInstance?
    {
        return instances.find { it.client == client }
    }

}