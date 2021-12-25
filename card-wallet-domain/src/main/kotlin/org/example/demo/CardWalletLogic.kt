package org.example.demo

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
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

    override fun addPass(id: UUID, newPass: Pass): Wallet {
        val wallet = repo.getWalletById(id).toDomain()
        val pass = newPass.toDomain()

        return wallet.addPass(pass)?.let { newWallet ->
            repo.update(newWallet.toDto())
        } ?: wallet.toDto()
    }

    override fun getWalletById(id: UUID): Wallet = repo.getWalletById(id)

    override fun debitPass(walletId: UUID, passId: UUID, amount: Int): Result4k<Pass, WalletError> {
        val wallet = repo.getWalletById(walletId).toDomain()
        return wallet.debitPass(passId.toPassIdDomain(), DebitAmount(amount))
            .map { updatedWallet ->
                repo.update(updatedWallet.toDto())
                val debitedPass = updatedWallet.findPass(passId.toPassIdDomain())!!.toDto()
                return Success(debitedPass)
            }
            .mapFailure { problem -> problem.toDtoFailure() }
    }
}
