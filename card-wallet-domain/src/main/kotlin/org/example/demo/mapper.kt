package org.example.demo

// mapping domain to DTOs
internal fun WalletDomain.toDto(): Wallet = Wallet(id.value, walletHolder, passes.map(PassDomain::toDto))
private fun PassDomain.toDto(): Pass = Pass(id.value, passName, passHolder, points)