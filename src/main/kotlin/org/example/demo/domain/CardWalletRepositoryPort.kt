package org.example.demo.domain

interface CardWalletRepositoryPort {
    fun save(wallet: Wallet): Wallet
    fun getAll(): List<Wallet>
    fun getWalletById(id: WalletId): Wallet
    fun update(wallet: Wallet): Wallet
}
