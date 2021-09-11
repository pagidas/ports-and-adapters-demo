package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

abstract class CardWalletRepositoryContract {

    abstract val cardWalletRepo: CardWalletRepositoryPort

    @Test
    fun `can persist wallets`() {
        val wallet1 = Wallet.empty(WalletId.random(), "John Doe")
            .also { cardWalletRepo.save(it)}
        val wallet2 = Wallet.empty(WalletId.random(), "Jane Doe")
            .also { cardWalletRepo.save(it)}

        assertThat(cardWalletRepo.getAll(), equalTo(listOf(wallet1, wallet2)))
    }

    @Test
    fun `can fetch wallet by id`() {
        val wallet = Wallet.empty(WalletId.random(), "Kostas Akrivos")
            .also { cardWalletRepo.save(it)}

        assertThat(cardWalletRepo.getWalletById(wallet.id), equalTo(wallet))
    }

    @Test
    fun `can update a wallet`() {
        val wallet = Wallet.empty(WalletId.random(), "Kostas Akrivos")
            .also { cardWalletRepo.save(it) }
        val pass = Pass(PassId.random(), "Tesco Clubcard", "Kostas Akrivos",)
        wallet.passes.add(pass)

        val updatedWallet = cardWalletRepo.update(wallet)
        val found = updatedWallet.passes.find { it.id == pass.id }

        assertThat(found, equalTo(pass))
    }

}