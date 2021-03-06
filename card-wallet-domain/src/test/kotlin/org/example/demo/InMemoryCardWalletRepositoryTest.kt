package org.example.demo

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class InMemoryCardWalletRepositoryTest: CardWalletRepositoryContract() {

    override val cardWalletRepo: CardWalletStoragePort = FakeCardWalletStorage()

    @Test
    fun `cannot save wallet that already exists`() {
        val wallet = WalletBuilder(walletHolder = "John Doe").build()
            .also { cardWalletRepo.save(it)}

        assertThrows<IllegalStateException> {
            cardWalletRepo.save(wallet)
        }
    }

    @Test
    fun `cannot fetch wallet by id when doesn't exist`() {
        assertThrows<NoSuchElementException> {
            cardWalletRepo.getWalletById(UUID.randomUUID())
        }
    }

    @Test
    fun `cannot update a wallet if it doesn't exist`() {
        val wallet = WalletBuilder(walletHolder = "Kostas Akrivos").build()

        assertThrows<NoSuchElementException> {
            cardWalletRepo.update(wallet)
        }
    }

}