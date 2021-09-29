package org.example.demo

import org.koin.core.context.startKoin
import org.koin.fileProperties

fun main() {
    val app: CardWalletApplication by lazy { CardWalletApplication() }

    startKoin {
        printLogger()
        fileProperties("/application.properties")

        // FIXME: 29/09/2021 Should be injected into nosql adapter.
        val mongoDbUrl = koin.getProperty<String>("mongo_db_url")
        println(mongoDbUrl)
        System.setProperty("MONGO_DB_URL", mongoDbUrl)

        modules(module)
    }

    app()
}