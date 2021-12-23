package org.example.demo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import org.example.demo.CardWalletJackson.auto
import org.http4k.core.*
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.uuid
import org.http4k.format.withStandardMappings
import org.http4k.lens.Path
import org.http4k.lens.uuid
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

private object CardWalletJackson: ConfigurableJackson(KotlinModule()
    .asConfigurable()
    .withStandardMappings()
    .uuid(::WalletId, WalletId::value)
    .uuid(::PassId, PassId::value)
    .done()
    .deactivateDefaultTyping()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
)

val walletLens = Body.auto<Wallet>().toLens()
val listWalletLens = Body.auto<List<Wallet>>().toLens()
val walletHolderLens = Body.auto<String>().toLens()
val walletIdPathLens = Path.uuid().of("walletId")
val passLens = Body.auto<Pass>().toLens()
val passIdPathLens = Path.uuid().of("passId")
val amountLens = Body.auto<Int>().toLens()
val notEnoughPointsLens = Body.auto<NotEnoughPoints>().toLens()
val healthCheckLens = Body.auto<Map<String, String>>().toLens()

internal object CardWalletWebController {

    operator fun invoke(cardWallet: CardWalletPort): RoutingHttpHandler {
        val healthRoute = "/health" bind { _:Request ->
            Response(Status.OK).with(healthCheckLens of mapOf("status" to "UP"))
        }

        val cardWalletRoutes = "/wallets" bind routes(
            createWallet(cardWallet),
            getWallets(cardWallet),
            addPass(cardWallet),
            getWalletById(cardWallet),
            debitPass(cardWallet)
        )

        return routes(healthRoute, cardWalletRoutes)
    }

    private fun createWallet(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/" bind Method.POST to { request: Request ->
            val walletHolder = walletHolderLens(request)
            Response(Status.CREATED).with(walletLens of cardWallet.createWallet(walletHolder))
        }

    private fun getWallets(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/" bind Method.GET to {
            Response(Status.OK).with(listWalletLens of cardWallet.list())
        }

    private fun addPass(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/{walletId}/passes" bind Method.POST to { request: Request ->
            val walletId = walletIdPathLens(request)
            val newPass = passLens(request)
            val result = cardWallet.addPass(walletId, newPass)
            Response(Status.CREATED).with(walletLens of result)
        }

    private fun getWalletById(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/{walletId}" bind Method.GET to { request: Request ->
            val walletId = walletIdPathLens(request)
            Response(Status.OK).with(walletLens of cardWallet.getWalletById(walletId))
        }

    private fun debitPass(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/{walletId}/passes/{passId}/debit" bind Method.POST to { request: Request ->
            val walletId = walletIdPathLens(request)
            val passId = passIdPathLens(request)
            val amount = amountLens(request)
            cardWallet.debitPass(walletId, passId, amount)
                .map { updatedPass: Pass ->
                    Response(Status.OK).with(passLens of updatedPass) }
                .recover { failure: NotEnoughPoints ->
                    Response(Status.UNPROCESSABLE_ENTITY).with(notEnoughPointsLens of failure) }
        }
}
