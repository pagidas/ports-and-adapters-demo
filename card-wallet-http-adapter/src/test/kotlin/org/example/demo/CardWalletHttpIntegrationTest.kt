package org.example.demo

import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ServerFilters
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class CardWalletHttpIntegrationTest: CardWalletContract() {

    private val httpServer: Http4kServer = CardWalletWebController(cardWalletLogicWithInMemoryRepository())
        .withFilter(ServerFilters.CatchLensFailure())
        .asServer(SunHttp())

    override val cardWallet: CardWalletPort = CardWalletHttpClient(
        ClientFilters.SetBaseUriFrom(Uri.of("http://localhost:${httpServer.port()}"))
            .then(JavaHttpClient()))

    @BeforeEach
    fun setUp() {
        httpServer.start()
    }

    @AfterEach
    fun tearDown() {
        httpServer.stop()
    }
}