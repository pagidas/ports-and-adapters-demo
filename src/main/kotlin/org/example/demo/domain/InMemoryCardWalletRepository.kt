package org.example.demo.domain

class InMemoryCardWalletRepository: CardWalletRepositoryPort {

    private val wallets = mutableMapOf<WalletId, Wallet>()

    override fun save(wallet: Wallet): Wallet {
        val saved = wallets.put(wallet.id, wallet)
        if (saved != null) {
            wallets.remove(wallet.id)
            throw IllegalStateException("Record with key: ${wallet.id.value} already exists!")
        }
        return wallet
    }

    override fun getAll(): List<Wallet> = wallets.values.toList()
}
