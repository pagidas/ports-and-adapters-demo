package org.example.demo

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import org.http4k.core.*

/**
 * This is an internal http implementation of the port
 * that tests and drives the http adapter of the domain port.
 *
 * Use [CardWalletHttpClientFactory] to target the api when running
 * on a host and port.
 */
class CardWalletHttpClient(private val http: HttpHandler): CardWalletPort {

    override fun createWallet(walletHolder: String): Wallet {
        val response = http(Request(Method.POST, "/wallets").with(walletHolderLens of walletHolder))
        return walletLens(response)
    }

    override fun list(): List<Wallet> {
        val response = http(Request(Method.GET, "/wallets"))
        return listWalletLens(response)
    }

    override fun addPass(id: WalletId, newPass: Pass): Wallet {
        val response = http(
            Request(Method.POST, "/wallets/{walletId}/passes")
            .with(walletIdPathLens of id.value, passLens of newPass))
        return walletLens(response)
    }

    override fun getWalletById(id: WalletId): Wallet {
        val response = http(Request(Method.GET, "/wallets/{walletId}").with(walletIdPathLens of id.value))
        return walletLens(response)
    }

    override fun debitPass(walletId: WalletId, passId: PassId, amount: Int): Result4k<Pass, NotEnoughPoints> {
        val response = http(
            Request(Method.POST, "/wallets/{walletId}/passes/{passId}/debit")
            .with(walletIdPathLens of walletId.value, passIdPathLens of passId.value, amountLens of amount))
        return when (response.status) {
            Status.OK -> Success(passLens(response))
            Status.UNPROCESSABLE_ENTITY -> Failure(notEnoughPointsLens(response))
            else -> throw RuntimeException("Not expected result response: $response")
        }
    }
}