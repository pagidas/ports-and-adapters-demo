package org.example.demo

import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

internal val walletLens = Body.auto<Wallet>().toLens()
internal val listWalletLens = Body.auto<List<Wallet>>().toLens()
internal val walletHolderLens = Body.auto<String>().toLens()

internal object CardWalletWebController {

    operator fun invoke(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/wallets" bind routes(
            createWallet(cardWallet),
            getWallets(cardWallet)
        )

    private fun createWallet(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/" bind Method.POST to { request: Request ->
            val walletHolder = walletHolderLens(request)
            Response(Status.CREATED).with(walletLens of cardWallet.createWallet(walletHolder))
        }

    private fun getWallets(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/" bind Method.GET to {
            Response(Status.OK).with(listWalletLens of cardWallet.list())
        }
}
