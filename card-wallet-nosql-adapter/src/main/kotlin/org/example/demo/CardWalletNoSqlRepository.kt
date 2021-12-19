package org.example.demo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import org.litote.kmongo.updateOne

fun createCardWalletNoSqlRepository(mongoConfig: MongoDbProperties): CardWalletRepositoryPort {
    return CardWalletNoSqlRepository(createMongoDbClient(mongoConfig))
}

class CardWalletNoSqlRepository internal constructor(mongoClient: MongoClient): CardWalletRepositoryPort {

    private val walletsCol: MongoCollection<Wallet> =
        mongoClient.getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)

    override fun save(wallet: Wallet): Wallet {
        walletsCol.insertOne(wallet)
        return wallet
    }

    override fun getAll(): List<Wallet> = walletsCol.find().toList()

    override fun getWalletById(id: WalletId): Wallet =
        walletsCol.find().find { it.id == id.value }
            ?: throw NoSuchElementException("Wallet record with key: $id does not exist.")

    override fun update(wallet: Wallet): Wallet {
        walletsCol.updateOne(Wallet::id eq wallet.id, wallet)
        return wallet
    }

}
