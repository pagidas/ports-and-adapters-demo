package org.example.demo

import java.util.*

class FakeCardWalletStorage: CardWalletStoragePort {

    private val wallets = mutableMapOf<UUID, Wallet>()

    override fun save(wallet: Wallet): Wallet =
        wallets.put(wallet.id, wallet)?.let {
            wallets.remove(wallet.id)
            throw IllegalStateException("Wallet record with key: ${wallet.id} already exists.")
        } ?: wallet

    override fun getAll(): List<Wallet> = wallets.values.toList()

    override fun getWalletById(id: UUID): Wallet =
        wallets[id] ?: throw NoSuchElementException("Wallet record with key: $id does not exist.")

    override fun update(wallet: Wallet): Wallet =
        wallets.put(wallet.id, wallet)?.let {
            wallet
        } ?: run {
            wallets.remove(wallet.id)
            throw NoSuchElementException("Wallet record with key: ${wallet.id} does not exist.")
        }
}