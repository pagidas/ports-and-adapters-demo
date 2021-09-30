package org.example.demo

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

class CardWalletNoSqlRepository(private val mongoDbConfig: MongoDbConfig): CardWalletRepositoryPort {

    private val walletsCol: MongoCollection<Wallet>

    init {
        walletsCol = KMongo.createClient(mongoDbConfig.url()).run {
            getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)
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

}

data class MongoDbConfig(private val host: String = "localhost", private val port: Int) {
    companion object {
        private const val PROTOCOL = "mongodb"

        const val CARD_WALLET_DB_NAME = "card_wallet"
        const val WALLETS_COLLECTION_NAME = "wallets"
    }

    fun url(): String = "$PROTOCOL://$host:$port"
}

