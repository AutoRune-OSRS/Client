package io.autorune.client.instance.account

import io.autorune.interaction.characteristic.Characteristics

class AccountProfile(private var credentials: AccountCredentials? = null) {

    lateinit var characteristics: Characteristics

    fun setCredentials(credentials: AccountCredentials) {

        this.credentials = credentials
        characteristics = Characteristics.loadCharacteristics(credentials.email)

    }

    fun getCredentials() : AccountCredentials? { return credentials }

}