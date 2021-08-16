package org.example.demo.domain

interface CardWalletPort {
    fun createWallet(walletHolder: String): Wallet
    fun list(): List<Wallet>
}
