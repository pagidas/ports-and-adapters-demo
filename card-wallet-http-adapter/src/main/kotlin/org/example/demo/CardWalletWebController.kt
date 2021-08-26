package org.example.demo

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
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
internal val walletIdPathLens = Path.map({ WalletId(UUID.fromString(it)) }, { it.value.toString() }).of("walletId")
internal val passLens = Body.auto<Pass>().toLens()
internal val passIdPathLens = Path.map({ PassId(UUID.fromString(it)) }, { it.value.toString() }).of("passId")
internal val amountLens = Body.auto<Int>().toLens()
internal val notEnoughPointsLens = Body.auto<NotEnoughPoints>().toLens()

internal object CardWalletWebController {

    operator fun invoke(cardWallet: CardWalletPort): RoutingHttpHandler =
        "/wallets" bind routes(
            createWallet(cardWallet),
            getWallets(cardWallet),
            addPass(cardWallet),
            getWalletById(cardWallet),
            debitPass(cardWallet)
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
        "/{walletId}/passes" bind Method.POST to { request: Request ->
            val walletId = walletIdPathLens(request)
            val newPass = passLens(request)
            Response(Status.CREATED).with(walletLens of cardWallet.addPass(walletId, newPass))
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
