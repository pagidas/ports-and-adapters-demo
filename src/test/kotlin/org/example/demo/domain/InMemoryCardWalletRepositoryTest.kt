package org.example.demo.domain

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryCardWalletRepositoryTest {

    private val repo = InMemoryCardWalletRepository()

    @Test
    fun `can persist wallets`() {
        val wallet1 = Wallet.empty(WalletId.random(), "John Doe")
            .also { repo.save(it)}
        val wallet2 = Wallet.empty(WalletId.random(), "Jane Doe")
            .also { repo.save(it)}

        assertThat(repo.getAll(), equalTo(listOf(wallet1, wallet2)))
    }

    @Test
    fun `cannot save wallet that already exists`() {
        val wallet = Wallet.empty(WalletId.random(), "John Doe")
            .also { repo.save(it)}

        assertThrows<NoSuchElementException> {
            repo.save(wallet)
        }
    }

    @Test
    fun `can fetch wallet by id`() {
        val wallet = Wallet.empty(WalletId.random(), "Kostas Akrivos")
            .also { repo.save(it)}

        assertThat(repo.getWalletById(wallet.id), equalTo(wallet))
    }

    @Test
    fun `can update a wallet`() {
        val wallet = Wallet.empty(WalletId.random(), "Kostas Akrivos")
            .also { repo.save(it) }
        val pass = Pass(PassId.random(), "Tesco Clubcard", "Kostas Akrivos",)
        wallet.passes.add(pass)

        val updatedWallet = repo.update(wallet)
        val found = updatedWallet.passes.find { it.id == pass.id }

        assertThat(found, equalTo(pass))
    }

    @Test
    fun `cannot fetch wallet by id when doesn't exist`() {
        assertThrows<NoSuchElementException> {
            repo.getWalletById(WalletId.random())
        }
    }

    @Test
    fun `cannot update a wallet if it doesn't exist`() {
        val wallet = Wallet.empty(WalletId.random(), "Kostas Akrivos")

        assertThrows<NoSuchElementException> {
            repo.update(wallet)
        }
    }

}