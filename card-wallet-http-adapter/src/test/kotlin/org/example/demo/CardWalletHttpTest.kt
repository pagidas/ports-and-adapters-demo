package org.example.demo

import org.http4k.filter.ServerFilters
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class CardWalletHttpTest: CardWalletContract() {

    private val httpServer: Http4kServer = CardWalletWebController(InMemoryCardWallet())
        .withFilter(ServerFilters.CatchLensFailure())
        .asServer(SunHttp())

    override val cardWallet: CardWalletPort =
        CardWalletHttpClientFactory.ofUri("http://localhost:${httpServer.port()}")

    @BeforeEach
    fun setUp() {
        httpServer.start()
    }

    @AfterEach
    fun tearDown() {
        httpServer.stop()
    }
}