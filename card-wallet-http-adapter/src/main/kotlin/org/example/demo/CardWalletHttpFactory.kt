package org.example.demo

import org.http4k.filter.ServerFilters
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer

class CardWalletHttpFactory {
    companion object {
        fun getService(cardWalletPort: CardWalletPort): Http4kServer =
            CardWalletWebController(cardWalletPort)
                .withFilter(ServerFilters.CatchLensFailure())
                .asServer(SunHttp())
    }
}