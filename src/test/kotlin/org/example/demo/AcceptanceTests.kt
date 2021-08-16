package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.example.demo.domain.CardWalletLogic
import org.example.demo.domain.CardWalletPort
import org.junit.jupiter.api.Test

class AcceptanceTests {

    private val cardWallet: CardWalletPort = CardWalletLogic()

    @Test
    fun `can create a wallet`() {
        val created = cardWallet.createWallet("Kostas Akrivos")

        val wallets = cardWallet.list()

        assertThat(wallets, equalTo(listOf(created)))
    }
}



