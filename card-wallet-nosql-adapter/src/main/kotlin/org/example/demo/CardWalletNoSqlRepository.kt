package org.example.demo

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

class CardWalletNoSqlRepository: CardWalletRepositoryPort {
    companion object {
        const val CARD_WALLET_DB_NAME = "card_wallet"
        const val WALLETS_COLUMN_NAME = "wallets"
        val mongoDbUrl: String by lazy { System.getProperty("MONGO_DB_URL") }
    }

    private val walletsCol: MongoCollection<Wallet>

    init {
        KMongo.createClient(mongoDbUrl).run {
            walletsCol = getDatabase(CARD_WALLET_DB_NAME).getCollection<Wallet>(WALLETS_COLUMN_NAME)
        }
    }

    override fun save(wallet: Wallet): Wallet {
        walletsCol.insertOne(wallet)
        return wallet
    }

    override fun getAll(): List<Wallet> = walletsCol.find().toList()

    override fun getWalletById(id: WalletId): Wallet =
        walletsCol.find().find { it.id == id }
            ?: throw NoSuchElementException("Wallet record with key: $id does not exist.")

    override fun update(wallet: Wallet): Wallet {
        walletsCol.updateOne(Wallet::id eq wallet.id, wallet)
        return wallet
    }

    /**
     * Used only for testing to clear the state in mongodb container.
     */
    fun deleteAll() { walletsCol.deleteMany(EMPTY_BSON) }
}

