package org.example.demo

import org.http4k.filter.ServerFilters
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer

class CardWalletHttpServer(cardWalletPort: CardWalletPort) {

    private val httpServer: Http4kServer = createHttpCardWallet(cardWalletPort)

    fun start() {
        httpServer.start()
    }
    fun stop() {
        httpServer.stop()
    }
    fun port(): Int = httpServer.port()
}

/**
 * This factory is responsible to construct the http service,
 * collecting the api endpoints, adding filters, and choosing
 * the http server at which the service will be running to.
 */
private fun createHttpCardWallet(cardWalletPort: CardWalletPort): Http4kServer =
    CardWalletWebController(cardWalletPort)
        .withFilter(ServerFilters.CatchLensFailure())
        .asServer(SunHttp())
