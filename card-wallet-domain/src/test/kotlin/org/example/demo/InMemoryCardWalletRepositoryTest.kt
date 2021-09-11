package org.example.demo

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryCardWalletRepositoryTest: CardWalletRepositoryContract() {

    override val cardWalletRepo: CardWalletRepositoryPort = InMemoryCardWalletRepository()

    @Test
    fun `cannot save wallet that already exists`() {
        val wallet = Wallet.empty(WalletId.random(), "John Doe")
            .also { cardWalletRepo.save(it)}

        assertThrows<IllegalStateException> {
            cardWalletRepo.save(wallet)
        }
    }

    @Test
    fun `cannot fetch wallet by id when doesn't exist`() {
        assertThrows<NoSuchElementException> {
            cardWalletRepo.getWalletById(WalletId.random())
        }
    }

    @Test
    fun `cannot update a wallet if it doesn't exist`() {
        val wallet = Wallet.empty(WalletId.random(), "Kostas Akrivos")

        assertThrows<NoSuchElementException> {
            cardWalletRepo.update(wallet)
        }
    }

}