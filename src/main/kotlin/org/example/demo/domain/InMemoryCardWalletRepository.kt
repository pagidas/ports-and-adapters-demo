package org.example.demo.domain

class InMemoryCardWalletRepository: CardWalletRepositoryPort {

    private val wallets = mutableMapOf<WalletId, Wallet>()

    override fun save(wallet: Wallet): Wallet =
        wallets.put(wallet.id, wallet).let { saved ->
            if (saved != null) {
                wallets.remove(wallet.id)
                throw IllegalStateException("Wallet record with key: ${wallet.id.value} already exists.")
            }
            wallet
        }

    override fun getAll(): List<Wallet> = wallets.values.toList()

    override fun getWalletById(id: WalletId): Wallet =
        wallets[id] ?: throw NoSuchElementException("Wallet record with key: $id does not exist.")

    override fun update(wallet: Wallet): Wallet =
        wallets.put(wallet.id, wallet).let { saved ->
            if (saved == null) {
                wallets.remove(wallet.id)
                throw IllegalStateException("Wallet record with key: ${wallet.id} does not exist.")
            }
            saved
        }
}
