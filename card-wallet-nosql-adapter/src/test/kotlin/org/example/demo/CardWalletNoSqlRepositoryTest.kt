package org.example.demo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

class CardWalletNoSqlRepositoryTest: CardWalletRepositoryContract() {

    override val cardWalletRepo: CardWalletNoSqlRepository = CardWalletNoSqlRepository(mongoDbConfig)

    companion object {
        lateinit var mongoDbConfig: MongoDbConfig

        @BeforeAll
        @JvmStatic
        fun setup() {
            TestContainersBase()
            mongoDbConfig = MongoDbConfig(port = TestContainersBase.mongoDbContainerPort)
        }
    }

    @AfterEach
    fun clean() {
        getWalletsCol().deleteMany(EMPTY_BSON)
    }

    @Test
    fun `cannot fetch wallet by id when doesn't exist`() {
        assertThrows<NoSuchElementException> {
            cardWalletRepo.getWalletById(WalletId.random())
        }
    }
}

internal fun getWalletsCol() = KMongo.createClient(TestContainersBase.mongoDbContainerUrl).run {
    getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)
}

