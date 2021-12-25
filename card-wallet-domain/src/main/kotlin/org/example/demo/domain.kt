package org.example.demo

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import org.example.demo.WalletProblem.PassNotEnoughPoints
import org.example.demo.WalletProblem.PassNotFound
import java.util.*

internal typealias PassesDomain = List<PassDomain>

internal data class WalletIdDomain(val value: UUID) {
    companion object {
        fun random(): WalletIdDomain = WalletIdDomain(UUID.randomUUID())
    }
}
internal data class PassIdDomain(val value: UUID)

internal data class WalletDomain(val id: WalletIdDomain, val walletHolder: String, val passes: PassesDomain) {
    companion object {
        fun empty(id: UUID, walletHolder: String): WalletDomain = WalletDomain(WalletIdDomain(id), walletHolder, emptyList())
    }

    fun findPass(passId: PassIdDomain): PassDomain? = passes.find { it.id == passId }

    fun addPass(pass: PassDomain): WalletDomain? =
        passes.find { it.id == pass.id }.let {
            if (it == null) copy(passes = passes + pass) else null
        }

    fun debitPass(passId: PassIdDomain, amount: DebitAmount): Result4k<WalletDomain, WalletProblem> =
        passes.find { it.id.value == passId.value }?.let { foundPass ->
            foundPass.debit(amount)?.let { updatedPass ->
                val newPasses = passes.toMutableList()
                    .also {
                        it.remove(foundPass)
                        it.add(updatedPass)
                    }
                Success(copy(passes = newPasses))
            } ?: Failure(PassNotEnoughPoints(passId, amount, foundPass.balance))
        } ?: Failure(PassNotFound(passId))
}

internal data class PassDomain(
    val id: PassIdDomain,
    val passName: String,
    val passHolder: String,
    val balance: PassBalance = PassBalance.Zero(),
) {
    fun debit(amount: DebitAmount): PassDomain? =
        if (amount <= balance) copy(balance = balance - amount) else null
}

internal data class PassBalance(val value: Int) {
    init {
        require(value >= 0) { "PassBalance can not be negative" }
    }

    operator fun minus(amount: DebitAmount): PassBalance = copy(value = value - amount.value)

    object Zero {
        operator fun invoke(): PassBalance = PassBalance(0)
    }
}

internal data class DebitAmount(val value: Int) {
    init {
        require(value > 0) { "DebitAmount can not be zero or negative" }
    }

    operator fun compareTo(balance: PassBalance): Int =
        if (value == balance.value) 0
        else if (value > balance.value) 1
        else -1
}

internal sealed class WalletProblem {
    data class PassNotEnoughPoints(
        val passId: PassIdDomain,
        val debitAmount: DebitAmount,
        val balance: PassBalance,
    ): WalletProblem() {
        init {
            require(debitAmount > balance) {
                "Problem: $this can only happen when debit amount is greater than the pass balance"
            }
        }
    }

    data class PassNotFound(val passId: PassIdDomain): WalletProblem()
}
