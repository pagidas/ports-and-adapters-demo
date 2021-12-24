package org.example.demo

import java.util.*

data class Wallet(val id: UUID, val walletHolder: String, val passes: List<Pass>)
data class Pass(val id: UUID, val passName: String, val passHolder: String, val points: Int = 0)
data class WalletError(val error: String, val data: WalletIssue) {
    sealed class WalletIssue {
        data class NotEnoughPoints(val passId: UUID, val debitAmount: Int, val balance: Int): WalletIssue()
        data class PassNotFound(val id: UUID): WalletIssue()
    }
}
