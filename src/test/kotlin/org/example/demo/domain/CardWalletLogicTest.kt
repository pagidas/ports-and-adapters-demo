package org.example.demo.domain

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.junit.jupiter.api.Test
import java.util.*

class CardWalletLogicTest: CardWalletContract() {

    private val walletId: WalletId
    override val cardWallet: CardWalletPort

    init {
        walletId = WalletId(UUID.fromString("ded99ee0-d211-4d9e-a5c1-d69a98fc2040"))
        cardWallet = CardWalletLogic({ walletId }, InMemoryCardWalletRepository())
    }

    @Test
    fun `can create an empty wallet`() {
        val wallet = cardWallet.createWallet("Kostas Akrivos")

        assertThat(wallet.passes, isEmpty)
    }

    @Test
    fun `can create a wallet generating a random id`() {
        val wallet = cardWallet.createWallet("Kostas Akrivos")

        assertThat(
            wallet,
            equalTo(Wallet(walletId, "Kostas Akrivos", LinkedList()))
        )
    }
}