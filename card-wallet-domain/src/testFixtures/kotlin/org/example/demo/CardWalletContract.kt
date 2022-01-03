package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.forkhandles.result4k.get
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
    fun `can not add a pass to a wallet when pass not found`() {
        val wallet = givenWalletWithPass(PassBuilder().build())

        val notUpdatedWallet = cardWallet.addPass(wallet.id, wallet.passes.random())

        assertThat(notUpdatedWallet, equalTo(wallet))
    }

    @Test
    fun `can debit pass points`() {
        val pass = PassBuilder(points = 70).build()
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.debitPass(wallet.id, pass.id, 50)

        assertThat((result.get() as Pass).points, equalTo(20))
    }

    @Test
    fun `can not debit more than balance`() {
        val pass = PassBuilder(points = 50).build()
        val wallet = givenWalletWithPass(pass)

        val result =  cardWallet.debitPass(wallet.id, pass.id, 51)

        val error = WalletErrorBuilder.ofPass(pass)
            .withNotEnoughPointsIssue(debitAmount = 51, balance = 50)
            .build()
        assertThat(result.get() as WalletError, equalTo(error))
    }

    @Test
    fun `can not debit pass when not found`() {
        val wallet = givenNewWallet()
        val pass = PassBuilder(points = 50).build()

        val result = cardWallet.debitPass(wallet.id, pass.id, 50)

        val error = WalletErrorBuilder.ofPass(pass)
            .withPassNotFoundIssue()
            .build()
        assertThat(result.get() as WalletError, equalTo(error))
    }

    private fun givenNewWallet(walletHolder: String = "John Doe"): Wallet = cardWallet.createWallet(walletHolder)

    private fun givenWalletWithPass(aPass: Pass): Wallet {
        val wallet = cardWallet.createWallet("John Doe")
        return cardWallet.addPass(wallet.id, aPass)
    }
}
