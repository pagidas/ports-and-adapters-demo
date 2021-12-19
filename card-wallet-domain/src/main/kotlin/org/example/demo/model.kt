package org.example.demo

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import java.util.*

typealias Passes = LinkedList<Pass>

data class Wallet(val id: UUID, val walletHolder: String, val passes: Passes) {
    companion object {
        fun empty(id: UUID, walletHolder: String) = Wallet(id, walletHolder, LinkedList())
    }
}
data class WalletId(val value: UUID) {
    companion object {
        fun random() = WalletId(UUID.randomUUID())
    }
}
data class Pass(val id: UUID, val passName: String, val passHolder: String, val points: Int = 0) {
    fun debit(amount: Int): Result4k<Pass, NotEnoughPoints> =
        if (amount <= points) Success(copy(points = points - amount))
        else Failure(NotEnoughPoints(id, amount, points))
}
data class PassId(val value: UUID) {
    companion object {
        fun random() = PassId(UUID.randomUUID())
    }
}
data class NotEnoughPoints(val passId: UUID, val debitAmount: Int, val balance: Int)
