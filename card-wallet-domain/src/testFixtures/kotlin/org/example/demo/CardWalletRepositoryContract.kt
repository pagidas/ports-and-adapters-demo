package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.util.*

abstract class CardWalletRepositoryContract {

    abstract val cardWalletRepo: CardWalletRepositoryPort

    @Test
    fun `can persist wallets`() {
        val wallet1 = WalletBuilder().build()
            .also { cardWalletRepo.save(it)}
        val wallet2 = WalletBuilder().build()
            .also { cardWalletRepo.save(it)}

        assertThat(cardWalletRepo.getAll(), equalTo(listOf(wallet1, wallet2)))
    }

    @Test
    fun `can fetch wallet by id`() {
        val wallet = WalletBuilder(walletHolder = "Kostas Akrivos").build()
            .also { cardWalletRepo.save(it)}

        assertThat(cardWalletRepo.getWalletById(wallet.id), equalTo(wallet))
    }

    @Test
    fun `can update a wallet`() {
        val wallet = WalletBuilder(walletHolder = "Kostas Akrivos").build()
            .also { cardWalletRepo.save(it) }
        val pass = Pass(UUID.randomUUID(), "Tesco Clubcard", "Kostas Akrivos",)
        val newWallet = wallet.copy(passes = wallet.passes + pass)

        val updatedWallet = cardWalletRepo.update(newWallet)
        val found = updatedWallet.passes.find { it.id == pass.id }

        assertThat(found, equalTo(pass))
    }

}