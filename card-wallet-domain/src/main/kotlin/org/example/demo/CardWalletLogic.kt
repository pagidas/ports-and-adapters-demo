package org.example.demo

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.peek

class CardWalletLogic(
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

    override fun debitPass(walletId: WalletId, passId: PassId, amount: Int): Result4k<Pass, NotEnoughPoints> {
        val wallet = repo.getWalletById(walletId)
        val foundPass = wallet.passes.first { it.id == passId }
        return foundPass.debit(amount).peek { updatedPass ->
            wallet.passes.remove(foundPass)
            wallet.passes.add(updatedPass)
            repo.update(wallet)
        }
    }
}
