package org.example.demo

import org.example.demo.WalletError.WalletIssue.NotEnoughPoints
import org.example.demo.WalletError.WalletIssue.PassNotFound
import java.util.*

private const val PASS_NOT_ENOUGH_POINTS_ERROR_MSG = "Insufficient balance."
private const val PASS_NOT_FOUND_ERROR_MSG = "Pass not found."

// mapping domain to DTOs
internal fun WalletDomain.toDto(): Wallet = Wallet(id.value, walletHolder, passes.map(PassDomain::toDto))
internal fun PassDomain.toDto(): Pass = Pass(id.value, passName, passHolder, balance.value)
internal fun WalletProblem.toDtoFailure(): WalletError {
    return when(this) {
        is WalletProblem.PassNotEnoughPoints ->
            WalletError(PASS_NOT_ENOUGH_POINTS_ERROR_MSG, NotEnoughPoints(passId.value, debitAmount.value, balance.value))
        is WalletProblem.PassNotFound ->
            WalletError(PASS_NOT_FOUND_ERROR_MSG, PassNotFound(passId.value))
    }
}

// mapping DTO to domain
internal fun Wallet.toDomain(): WalletDomain = WalletDomain(id.toWalletIdDomain(), walletHolder, passes.map(Pass::toDomain))
internal fun Pass.toDomain(): PassDomain = PassDomain(id.toPassIdDomain(), passName, passHolder, points.toPassBalance())
private fun Int.toPassBalance(): PassBalance = PassBalance(this)
private fun UUID.toWalletIdDomain(): WalletIdDomain = WalletIdDomain(this)
internal fun UUID.toPassIdDomain(): PassIdDomain = PassIdDomain(this)