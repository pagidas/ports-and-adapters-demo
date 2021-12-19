package org.example.demo

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.peek
import java.util.*

class CardWalletLogic(
    private val idFactory: () -> UUID = { WalletIdDomain.random().value },
    private val repo: CardWalletRepositoryPort
): CardWalletPort {

    override fun createWallet(walletHolder: String): Wallet =
        WalletDomain.empty(idFactory(), walletHolder)
            .toDto()
            .also { repo.save(it) }

    override fun list(): List<Wallet> = repo.getAll()

    override fun addPass(id: WalletId, newPass: Pass): Wallet {
        val wallet = repo.getWalletById(id).toDomain()
        val pass = newPass.toDomain()

        return wallet.addPass(pass)?.let { newWallet ->
            repo.update(newWallet.toDto())
        } ?: wallet.toDto()
    }

    override fun getWalletById(id: WalletId): Wallet = repo.getWalletById(id)

    override fun debitPass(walletId: WalletId, passId: PassId, amount: Int): Result4k<Pass, NotEnoughPoints> {
        val wallet = repo.getWalletById(walletId)
        val foundPass = wallet.passes.first { it.id == passId.value }
        return foundPass.debit(amount).peek { updatedPass ->
            val newPasses = wallet.passes.toMutableList()
            newPasses.remove(foundPass)
            newPasses.add(updatedPass)
            repo.update(wallet.copy(passes = newPasses))
        }
    }
}
