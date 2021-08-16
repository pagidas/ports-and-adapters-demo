package org.example.demo.domain

internal class CardWalletLogic(
    private val idFactory: () -> WalletId = { WalletId.random() },
    private val repo: CardWalletRepositoryPort
): CardWalletPort {

    override fun createWallet(walletHolder: String): Wallet =
        Wallet.empty(idFactory(), walletHolder)
            .also { repo.save(it) }

    override fun list(): List<Wallet> = repo.getAll()

    override fun addPass(id: WalletId, newPass: Pass): Wallet {
        val wallet = repo.getWalletById(id)
        wallet.passes.add(newPass)
        return repo.update(wallet)
    }

    override fun getWalletById(id: WalletId): Wallet = repo.getWalletById(id)
}
