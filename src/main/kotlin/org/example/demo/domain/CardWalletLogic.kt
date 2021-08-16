package org.example.demo.domain

class CardWalletLogic(
    val idFactory: () -> WalletId = { WalletId.random() },
    private val repo: CardWalletRepositoryPort
): CardWalletPort {

    override fun createWallet(walletHolder: String): Wallet {
        return Wallet(idFactory(), walletHolder)
            .also { repo.save(it) }
    }

    override fun list(): List<Wallet> = repo.getAll()
}
