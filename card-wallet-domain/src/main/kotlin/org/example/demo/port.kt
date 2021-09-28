package org.example.demo

import dev.forkhandles.result4k.Result4k

/**
 * Primary/driving/input port that shapes how to communicate with the domain.
 */
interface CardWalletPort {
    fun createWallet(walletHolder: String): Wallet
    fun list(): List<Wallet>
    fun addPass(id: WalletId, newPass: Pass): Wallet
    fun getWalletById(id: WalletId): Wallet
    fun debitPass(walletId: WalletId, passId: PassId, amount: Int): Result4k<Pass, NotEnoughPoints>
}

/**
 * Secondary/driven/output port that shapes how domain stores the state.
 */
interface CardWalletRepositoryPort {
    fun save(wallet: Wallet): Wallet
    fun getAll(): List<Wallet>
    fun getWalletById(id: WalletId): Wallet
    fun update(wallet: Wallet): Wallet
}

