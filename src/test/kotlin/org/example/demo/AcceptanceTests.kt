package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.example.demo.domain.*
import org.junit.jupiter.api.Test

// TODO: 16/08/2021 This will be a contract test abstract class where test suites need to comply with it.
class AcceptanceTests {

    private val repo: CardWalletRepositoryPort = InMemoryCardWalletRepository()
    private val cardWallet: CardWalletPort = CardWalletLogic(repo = repo)

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



