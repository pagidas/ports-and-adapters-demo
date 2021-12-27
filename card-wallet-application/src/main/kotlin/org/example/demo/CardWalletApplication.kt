package org.example.demo

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

/**
 * Definition of the module using dependency injection.
 */
val module = module {
    single<MongoDbProperties> { MongoDbConfig(port = getProperty("mongo_db_port").toInt()) }
    single { createCardWalletNoSqlRepository(get()) }
    single<CardWalletPort> { CardWalletLogic(storage = get()) }
    single { CardWalletHttpServer(get()) }
}

/**
 * Typed application which expects an http server defined in the module.
 */
class CardWalletApplication: KoinComponent {

    private val httpServer: CardWalletHttpServer by inject()

    operator fun invoke() {
        httpServer.start()
    }
}