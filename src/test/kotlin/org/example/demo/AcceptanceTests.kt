package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.example.demo.domain.InMemoryCardWalletRepository
import org.example.demo.domain.CardWalletLogic
import org.example.demo.domain.CardWalletPort
import org.example.demo.domain.CardWalletRepositoryPort
import org.junit.jupiter.api.Test

class AcceptanceTests {

    private val repo: CardWalletRepositoryPort = InMemoryCardWalletRepository()
    private val cardWallet: CardWalletPort = CardWalletLogic(repo = repo)

    @Test
    fun `can create a wallet`() {
        val created = cardWallet.createWallet("Kostas Akrivos")

        val wallets = cardWallet.list()

        assertThat(wallets, equalTo(listOf(created)))
    }
}



