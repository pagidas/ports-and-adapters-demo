package org.example.demo.domain

/**
 * Use this to refer to domain implementation with in-memory database, easy for testing.
 */
fun cardWalletLogicWithInMemoryRepository(): CardWalletPort = CardWalletLogic(repo = InMemoryCardWalletRepository())

/**
 * Primary/driving/input port that shapes how to communicate with the domain.
 */
interface CardWalletPort {
    fun createWallet(walletHolder: String): Wallet
    fun list(): List<Wallet>
    fun addPass(id: WalletId, newPass: Pass): Wallet
    fun getWalletById(id: WalletId): Wallet
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

