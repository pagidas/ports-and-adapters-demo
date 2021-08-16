package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.example.demo.domain.CardWalletPort
import org.example.demo.domain.Pass
import org.example.demo.domain.PassId
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
        val givenWallet = cardWallet.createWallet("Kostas Akrivos")
        val newPass = Pass(PassId.random(), "Kostas Akrivos", "Tesco Clubcard")

        val updatedWallet = cardWallet.addPass(givenWallet.id, newPass)

        assertThat(newPass, equalTo(updatedWallet.passes.find { it.id == newPass.id }))
    }
}



