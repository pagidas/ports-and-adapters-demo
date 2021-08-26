package org.example.demo

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
        val newPass = aPass()

        val updatedWallet = cardWallet.addPass(wallet.id, newPass)
        val foundPass = updatedWallet.passes.find { it.id == newPass.id }

        assertThat(foundPass, equalTo(newPass))
    }

    @Test
    fun `can debit pass points`() {
        val pass = aPass(points = 70)
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.debitPass(wallet.id, pass.id, 50)

        assertThat(result.valueOrNull()?.points, equalTo(20))
    }

    @Test
    fun `can not debit more than balance`() {
        val pass = aPass(points = 50)
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.debitPass(wallet.id, pass.id, 51)

        assertThat(result.failureOrNull(), equalTo(NotEnoughPoints(pass.id, 51, 50)))
    }

    private fun givenNewWallet(walletHolder: String = "John Doe"): Wallet = cardWallet.createWallet(walletHolder)

    private fun givenWalletWithPass(aPass: Pass): Wallet =
        cardWallet.createWallet("John Doe").also {
            cardWallet.addPass(it.id, aPass)
        }
}
