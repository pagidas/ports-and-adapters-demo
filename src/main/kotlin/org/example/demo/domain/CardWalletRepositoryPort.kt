package org.example.demo.domain

interface CardWalletRepositoryPort {
    fun save(wallet: Wallet): Wallet
    fun getAll(): List<Wallet>
}
