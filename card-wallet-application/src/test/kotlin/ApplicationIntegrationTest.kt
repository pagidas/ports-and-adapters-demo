import com.mongodb.client.MongoCollection
import org.example.demo.*
import org.http4k.server.Http4kServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationIntegrationTest: KoinTest, CardWalletContract() {

    private val httpServer: Http4kServer by inject()

    private val mongoDb: MongoDbDockerContainer = MongoDbDockerContainer

    override val cardWallet: CardWalletPort

    init {
        startKoin {
            printLogger()
            properties(mapOf("mongo_db_port" to mongoDb.port.toString()))
            modules(module)
        }
        httpServer.start()
        cardWallet = CardWalletHttpClientFactory.ofUri("http://localhost:${httpServer.port()}")
    }

    @AfterEach
    fun clean() {
        getWalletsCol().deleteMany(EMPTY_BSON)
    }

    private fun getWalletsCol(): MongoCollection<Wallet> = KMongo.createClient(mongoDb.url).run {
        getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)
    }
}