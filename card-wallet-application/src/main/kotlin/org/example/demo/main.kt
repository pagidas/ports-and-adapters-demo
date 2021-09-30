package org.example.demo

import org.koin.core.context.startKoin
import org.koin.fileProperties

fun main() {
    val app: CardWalletApplication by lazy { CardWalletApplication() }

    startKoin {
        printLogger()
        fileProperties("/application.properties")
        modules(module)
    }

    app()
}