package org.example.demo

import dev.forkhandles.result4k.Result4k
import java.util.*

/**
 * Primary/driving/input port that shapes how to communicate with the domain.
 */
interface CardWalletPort {
    fun createWallet(walletHolder: String): Wallet
    fun list(): List<Wallet>
    fun addPass(id: UUID, newPass: Pass): Wallet
    fun getWalletById(id: UUID): Wallet
    fun debitPass(walletId: UUID, passId: UUID, amount: Int): Result4k<Pass, NotEnoughPoints>
}

/**
 * Secondary/driven/output port that shapes how domain stores the state.
 */
interface CardWalletRepositoryPort {
    fun save(wallet: Wallet): Wallet
    fun getAll(): List<Wallet>
    fun getWalletById(id: UUID): Wallet
    fun update(wallet: Wallet): Wallet
}

