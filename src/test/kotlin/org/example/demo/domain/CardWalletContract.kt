package org.example.demo.domain

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.forkhandles.result4k.failureOrNull
import dev.forkhandles.result4k.valueOrNull
import org.junit.jupiter.api.Test

abstract class CardWalletContract {

    abstract val cardWallet: CardWalletPort

    @Test
    fun `can create a wallet`() {
        val created = cardWallet.createWallet("Kostas Akrivos")

        val wallets = cardWallet.list()

        assertThat(wallets, equalTo(listOf(created)))
    }

    @Test
    fun `can add a pass to a wallet`() {
        val wallet = givenNewWallet()
        val newPass = Pass(PassId.random(), "Tesco Clubcard", "Kostas Akrivos")

        val updatedWallet = cardWallet.addPass(wallet.id, newPass)
        val foundPass = updatedWallet.passes.find { it.id == newPass.id }

        assertThat(foundPass, equalTo(newPass))
    }

    @Test
    fun `can credit pass points`() {
        val pass = Pass(PassId.random(), "Tesco Clubcard", "John Doe", 70)
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.creditPass(wallet.id, pass.id, 50)

        assertThat(result.valueOrNull()?.points, equalTo(20))
    }

    @Test
    fun `can not credit more than balance`() {
        val pass = Pass(PassId.random(), "Tesco Clubcard", "John Doe", 50)
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.creditPass(wallet.id, pass.id, 51)

        assertThat(result.failureOrNull(), equalTo(NotEnoughPoints(pass.id, 51, 50)))
    }

    private fun givenNewWallet(walletHolder: String = "John Doe"): Wallet = cardWallet.createWallet(walletHolder)

    private fun givenWalletWithPass(aPass: Pass): Wallet =
        cardWallet.createWallet("John Doe").also {
            cardWallet.addPass(it.id, aPass)
        }
}
