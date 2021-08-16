package org.example.demo.domain

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.junit.jupiter.api.Test

class CardWalletLogicTest: CardWalletContract() {

    private val repo = InMemoryCardWalletRepository()
    override val cardWallet: CardWalletPort = CardWalletLogic(repo = repo)

    @Test
    fun `can create an empty wallet`() {
        val wallet = cardWallet.createWallet("Kostas Akrivos")

        assertThat(wallet.walletHolder, equalTo("Kostas Akrivos"))
        assertThat(wallet.passes, isEmpty)
    }
}