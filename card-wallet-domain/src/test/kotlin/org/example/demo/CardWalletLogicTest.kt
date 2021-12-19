package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.junit.jupiter.api.Test
import java.util.*

class CardWalletLogicTest: CardWalletContract() {

    private val walletId: UUID
    override val cardWallet: CardWalletPort

    init {
        walletId = UUID.fromString("ded99ee0-d211-4d9e-a5c1-d69a98fc2040")
        cardWallet = CardWalletLogic({ walletId }, FakeCardWalletRepository())
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

    @Test
    fun `can remove old pass from wallet when debiting points`() {
        val wallet = cardWallet.createWallet("Kostas Akrivos")
        val pass = aPass(points = 20)
        cardWallet.addPass(WalletId(wallet.id), pass)

        cardWallet.debitPass(WalletId(wallet.id), PassId(pass.id), 10)
        val updatedWallet = cardWallet.getWalletById(WalletId(wallet.id))

        assertThat(updatedWallet.passes.size, equalTo(1))
        assertThat(updatedWallet.passes.first(), equalTo(pass.copy(points = 10)))
    }
}