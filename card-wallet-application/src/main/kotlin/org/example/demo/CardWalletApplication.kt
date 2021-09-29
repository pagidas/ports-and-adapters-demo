package org.example.demo

import org.http4k.server.Http4kServer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

val module = module {
    single<CardWalletRepositoryPort> { CardWalletNoSqlRepository() }
    single<CardWalletPort> { CardWalletLogic(repo = get()) }
    single { CardWalletHttpFactory.getService(get()) }
}

class CardWalletApplication: KoinComponent {

    private val httpServer: Http4kServer by inject()

    operator fun invoke() {
        httpServer.start()
    }
}