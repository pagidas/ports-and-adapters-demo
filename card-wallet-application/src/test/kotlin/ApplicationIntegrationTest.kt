import com.mongodb.client.MongoCollection
import org.example.demo.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationIntegrationTest: CardWalletContract() {

    private val app: CardWalletApplication by lazy { CardWalletApplication() }

    private val mongoDb: MongoDbDockerContainer = MongoDbDockerContainer

    override val cardWallet: CardWalletPort

    init {
        startKoin {
            printLogger()
            properties(mapOf("mongo_db_port" to mongoDb.port.toString()))
            modules(module)
        }
        app()
        cardWallet = CardWalletHttpClientFactory.ofUri("http://localhost:${app.getHttpPort()}")
    }

    @AfterEach
    fun clean() {
        getWalletsCol().deleteMany(EMPTY_BSON)
    }

    private fun getWalletsCol(): MongoCollection<Wallet> = KMongo.createClient(mongoDb.url).run {
        getDatabase(MongoDbConfig.CARD_WALLET_DB_NAME).getCollection<Wallet>(MongoDbConfig.WALLETS_COLLECTION_NAME)
    }
}