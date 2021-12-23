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
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardWalletNoSqlRepositoryTest: CardWalletRepositoryContract() {

    private val mongoDb: MongoDbDockerContainer = MongoDbDockerContainer

    private val mongoConfig: MongoDbProperties = object: MongoDbProperties {
        override fun getUrl(): String = "mongodb://localhost:${mongoDb.port}"
    }

    override val cardWalletRepo: CardWalletRepositoryPort = createCardWalletNoSqlRepository(mongoConfig)

    @AfterEach
    fun clean() {
        getWalletsCol().deleteMany(EMPTY_BSON)
    }

    @Test
    fun `cannot fetch wallet by id when doesn't exist`() {
        assertThrows<NoSuchElementException> {
            cardWalletRepo.getWalletById(UUID.randomUUID())
        }
    }

    private fun getWalletsCol(): MongoCollection<Wallet> = KMongo.createClient(mongoDb.url).run {
        getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)
    }
}

