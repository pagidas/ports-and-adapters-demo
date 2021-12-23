package org.example.demo

import java.util.*

// mapping domain to DTOs
internal fun WalletDomain.toDto(): Wallet = Wallet(id.value, walletHolder, passes.map(PassDomain::toDto))
internal fun PassDomain.toDto(): Pass = Pass(id.value, passName, passHolder, balance.value)
internal fun WalletProblem.toDtoFailure(): NotEnoughPoints {
    return when(this) {
        is WalletProblem.PassNotEnoughPoints -> NotEnoughPoints(passId.value, debitAmount.value, balance.value)
        // TODO: 23/12/2021 Handle this scenario by expanding the api to include a more open problem.
        is WalletProblem.PassNotFound -> throw IllegalStateException()
    }
}

// mapping DTO to domain
internal fun Wallet.toDomain(): WalletDomain = WalletDomain(id.toWalletIdDomain(), walletHolder, passes.map(Pass::toDomain))
internal fun Pass.toDomain(): PassDomain = PassDomain(id.toPassIdDomain(), passName, passHolder, points.toPassBalance())
private fun Int.toPassBalance(): PassBalance = PassBalance(this)
private fun UUID.toWalletIdDomain(): WalletIdDomain = WalletIdDomain(this)
internal fun UUID.toPassIdDomain(): PassIdDomain = PassIdDomain(this)