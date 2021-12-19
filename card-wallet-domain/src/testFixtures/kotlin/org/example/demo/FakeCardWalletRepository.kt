package org.example.demo

class FakeCardWalletRepository: CardWalletRepositoryPort {

    private val wallets = mutableMapOf<WalletId, Wallet>()

    override fun save(wallet: Wallet): Wallet =
        wallets.put(WalletId(wallet.id), wallet)?.let {
            wallets.remove(WalletId(wallet.id))
            throw IllegalStateException("Wallet record with key: ${wallet.id} already exists.")
        } ?: wallet

    override fun getAll(): List<Wallet> = wallets.values.toList()

    override fun getWalletById(id: WalletId): Wallet =
        wallets[id] ?: throw NoSuchElementException("Wallet record with key: $id does not exist.")

    override fun update(wallet: Wallet): Wallet =
        wallets.put(WalletId(wallet.id), wallet)?.let {
            wallet
        } ?: run {
            wallets.remove(WalletId(wallet.id))
            throw NoSuchElementException("Wallet record with key: ${wallet.id} does not exist.")
        }
}