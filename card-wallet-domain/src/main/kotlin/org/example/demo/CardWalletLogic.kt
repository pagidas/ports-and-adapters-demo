package org.example.demo

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import java.util.*

class CardWalletLogic(
    private val idFactory: () -> UUID = { WalletIdDomain.random().value },
    private val storage: CardWalletStoragePort
): CardWalletPort {

    override fun createWallet(walletHolder: String): Wallet =
        WalletDomain.empty(idFactory(), walletHolder)
            .toDto()
            .also { storage.save(it) }

    override fun list(): List<Wallet> = storage.getAll()

    override fun addPass(id: UUID, newPass: Pass): Wallet {
        val wallet = storage.getWalletById(id).toDomain()
        val pass = newPass.toDomain()

        return wallet.addPass(pass)?.let { newWallet ->
            storage.update(newWallet.toDto())
        } ?: wallet.toDto()
    }

    override fun getWalletById(id: UUID): Wallet = storage.getWalletById(id)

    override fun debitPass(walletId: UUID, passId: UUID, amount: Int): Result4k<Pass, WalletError> {
        val wallet = storage.getWalletById(walletId).toDomain()
        return wallet.debitPass(passId.toPassIdDomain(), DebitAmount(amount))
            .map { updatedWallet ->
                val debitedPass = storage.update(updatedWallet.toDto())
                    .toDomain()
                    .findPass(passId.toPassIdDomain())!!
                    .toDto()
                return Success(debitedPass)
            }
            .mapFailure { problem -> problem.toDtoFailure() }
    }
}
