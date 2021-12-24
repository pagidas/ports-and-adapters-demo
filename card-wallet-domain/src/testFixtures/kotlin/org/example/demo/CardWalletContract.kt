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
        val newPass = PassBuilder().build()

        val updatedWallet = cardWallet.addPass(wallet.id, newPass)
        val foundPass = updatedWallet.passes.find { it.id == newPass.id }

        assertThat(foundPass, equalTo(newPass))
    }

    @Test
    fun `can debit pass points`() {
        val pass = PassBuilder(points = 70).build()
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.debitPass(wallet.id, pass.id, 50)

        assertThat(result.valueOrNull()?.points, equalTo(20))
    }

    @Test
    fun `can not debit more than balance`() {
        val pass = PassBuilder(points = 50).build()
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.debitPass(wallet.id, pass.id, 51)

        val error = WalletErrorBuilder.ofPass(pass)
            .withNotEnoughPointsIssue(debitAmount = 51, balance = 50)
            .build()
        assertThat(result.failureOrNull(), equalTo(error))
    }

    @Test
    fun `can not debit pass when not found`() {
        val wallet = givenNewWallet()
        val pass = PassBuilder(points = 50).build()

        val result = cardWallet.debitPass(wallet.id, pass.id, 50)

        val error = WalletErrorBuilder.ofPass(pass)
            .withPassNotFoundIssue()
            .build()
        assertThat(result.failureOrNull(), equalTo(error))
    }

    private fun givenNewWallet(walletHolder: String = "John Doe"): Wallet = cardWallet.createWallet(walletHolder)

    private fun givenWalletWithPass(aPass: Pass): Wallet =
        cardWallet.createWallet("John Doe").also {
            cardWallet.addPass(it.id, aPass)
        }
}
