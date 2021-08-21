package org.example.demo

import dev.forkhandles.result4k.Result4k
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.with

internal class CardWalletHttpClient(private val http: HttpHandler): CardWalletPort {

    override fun createWallet(walletHolder: String): Wallet {
        val response = http(Request(Method.POST, "/wallets").with(walletHolderLens of walletHolder))
        return walletLens(response)
    }

    override fun list(): List<Wallet> {
        val response = http(Request(Method.GET, "/wallets"))
        return listWalletLens(response)
    }

    override fun addPass(id: WalletId, newPass: Pass): Wallet {
        val response = http(Request(Method.POST, "/wallets/{id}/passes")
            .with(walletIdLens of id, passLens of newPass))
        return walletLens(response)
    }

    override fun getWalletById(id: WalletId): Wallet {
        val response = http(Request(Method.GET, "wallets/{id}").with(walletIdLens of id))
        return walletLens(response)
    }

    override fun creditPass(walletId: WalletId, passId: PassId, amount: Int): Result4k<Pass, NotEnoughPoints> {
        TODO("Not yet implemented")
    }
}