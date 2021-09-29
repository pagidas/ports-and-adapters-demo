package org.example.demo

import org.koin.core.context.startKoin

fun main() {
    // FIXME: 29/09/2021 Should come from a config
    System.setProperty("MONGO_DB_URL", "mongodb://localhost:8080")
    val app: CardWalletApplication by lazy { CardWalletApplication() }

    startKoin {
        printLogger()
        modules(module)
    }

    app()
}