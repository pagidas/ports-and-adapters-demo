package org.example.demo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CardWalletNoSqlRepositoryTest: CardWalletContract() {

    private val cardWalletRepo: CardWalletNoSqlRepository = CardWalletNoSqlRepository()
    override val cardWallet: CardWalletPort = CardWalletLogic(repo = cardWalletRepo)

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            TestContainersBase()
            System.setProperty("MONGO_DB_URL", TestContainersBase.mongoDbContainerUrl)
        }
    }

    @AfterEach
    fun clean() {
        cardWalletRepo.deleteAll()
    }

    @Test
    fun `cannot fetch wallet by id when doesn't exist`() {
        assertThrows<NoSuchElementException> {
            cardWalletRepo.getWalletById(WalletId.random())
        }
    }
}

