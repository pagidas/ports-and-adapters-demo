package org.example.demo

import java.util.*

// mapping domain to DTOs
internal fun WalletDomain.toDto(): Wallet = Wallet(id.value, walletHolder, passes.map(PassDomain::toDto))
private fun PassDomain.toDto(): Pass = Pass(id.value, passName, passHolder, balance.value)

// mapping DTO to domain
internal fun Wallet.toDomain(): WalletDomain = WalletDomain(id.toWalletIdDomain(), walletHolder, passes.map(Pass::toDomain))
internal fun Pass.toDomain(): PassDomain = PassDomain(id.toPassIdDomain(), passName, passHolder, points.toPassBalance())
private fun Int.toPassBalance(): PassBalance = PassBalance(this)
private fun UUID.toWalletIdDomain(): WalletIdDomain = WalletIdDomain(this)
private fun UUID.toPassIdDomain(): PassIdDomain = PassIdDomain(this)