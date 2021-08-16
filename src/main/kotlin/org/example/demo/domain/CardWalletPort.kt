package org.example.demo.domain

interface CardWalletPort {
    fun createWallet(walletHolder: String): Wallet
    fun list(): List<Wallet>
    fun addPass(id: WalletId, newPass: Pass): Wallet
    fun getWalletById(id: WalletId): Wallet
}
