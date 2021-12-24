package org.example.demo

import java.util.*

data class Wallet(val id: UUID, val walletHolder: String, val passes: List<Pass>) {
    companion object {
        fun empty(id: UUID, walletHolder: String) = Wallet(id, walletHolder, emptyList())
    }
}
data class Pass(val id: UUID, val passName: String, val passHolder: String, val points: Int = 0)
data class NotEnoughPoints(val passId: UUID, val debitAmount: Int, val balance: Int)
