package org.example.demo

import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.lens.Path
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.util.*

internal val walletLens = Body.auto<Wallet>().toLens()
internal val listWalletLens = Body.auto<List<Wallet>>().toLens()
internal val walletHolderLens = Body.auto<String>().toLens()
internal val walletIdLens = Path.map({ WalletId(UUID.fromString(it)) }, { it.value.toString() }).of("id")
internal val passLens = Body.auto<Pass>().toLens()

internal object CardWalletWebController {

    operator fun invoke(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/wallets" bind routes(
            createWallet(cardWallet),
            getWallets(cardWallet),
            addPass(cardWallet),
            getWalletById(cardWallet)
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

    private fun addPass(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/{id}/passes" bind Method.POST to { request: Request ->
            val walletId = walletIdLens(request)
            val newPass = passLens(request)
            Response(Status.CREATED).with(walletLens of cardWallet.addPass(walletId, newPass))
        }

    private fun getWalletById(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/{id}" bind Method.GET to { request: Request ->
            val walletId = walletIdLens(request)
            Response(Status.OK).with(walletLens of cardWallet.getWalletById(walletId))
        }
}
