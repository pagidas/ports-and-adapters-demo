package org.example.demo

import org.http4k.filter.ServerFilters
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer

/**
 * This factory is responsible to construct the http service,
 * collecting the api endpoints, adding filters, and choosing
 * the http server at which the service will be running to.
 */
class CardWalletHttpFactory {
    companion object {
        fun getService(cardWalletPort: CardWalletPort): Http4kServer =
            CardWalletWebController(cardWalletPort)
                .withFilter(ServerFilters.CatchLensFailure())
                .asServer(SunHttp())
    }
}