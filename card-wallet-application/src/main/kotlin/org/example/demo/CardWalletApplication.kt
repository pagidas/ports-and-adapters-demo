package org.example.demo

import org.http4k.server.Http4kServer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

/**
 * Definition of the module using dependency injection.
 */
val module = module {
    single { MongoDbConfig(port = getProperty("mongo_db_port").toInt()) }
    single<CardWalletRepositoryPort> { CardWalletNoSqlRepository(get()) }
    single<CardWalletPort> { CardWalletLogic(repo = get()) }
    single { CardWalletHttpFactory.getService(get()) }
}

/**
 * Typed application which expects an http server defined in the module.
 */
class CardWalletApplication: KoinComponent {

    private val httpServer: Http4kServer by inject()

    operator fun invoke() {
        httpServer.start()
    }
}