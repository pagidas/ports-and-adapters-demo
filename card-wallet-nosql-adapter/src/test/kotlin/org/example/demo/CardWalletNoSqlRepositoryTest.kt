package org.example.demo

import MongoDbDockerContainer
import com.mongodb.client.MongoCollection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardWalletNoSqlRepositoryTest: CardWalletRepositoryContract() {

    private val mongoDb: MongoDbDockerContainer = MongoDbDockerContainer

    override val cardWalletRepo: CardWalletNoSqlRepository =
        CardWalletNoSqlRepository(MongoDbConfig(port = mongoDb.port))

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

    private fun getWalletsCol(): MongoCollection<Wallet> = KMongo.createClient(mongoDb.url).run {
        getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)
    }
}

