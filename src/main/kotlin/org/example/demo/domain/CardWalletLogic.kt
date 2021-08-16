package org.example.demo.domain

class CardWalletLogic(val idFactory: () -> WalletId = { WalletId.random() }): CardWalletPort {
    private val wallets = mutableMapOf<WalletId, Wallet>()

    override fun createWallet(walletHolder: String): Wallet {
        return Wallet(idFactory(), walletHolder)
            .also { wallets[it.id] = it }
    }

    override fun list(): List<Wallet> = wallets.values.toList()
}
