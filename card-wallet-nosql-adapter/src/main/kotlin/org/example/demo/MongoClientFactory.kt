package org.example.demo

import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo

internal fun createMongoDbClient(mongoConfig: MongoDbProperties): MongoClient = KMongo.createClient(mongoConfig.getUrl())

interface MongoDbProperties {
    fun getUrl(): String
}

data class MongoDbConfig(private val host: String = "localhost", private val port: Int): MongoDbProperties {
    companion object {
        private const val PROTOCOL = "mongodb"

        const val CARD_WALLET_DB_NAME = "card_wallet"
        const val WALLETS_COLLECTION_NAME = "wallets"
    }

    override fun getUrl(): String = "$PROTOCOL://$host:$port"
}