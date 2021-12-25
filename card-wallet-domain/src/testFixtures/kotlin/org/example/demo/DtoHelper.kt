package org.example.demo

import org.example.demo.WalletError.WalletIssue
import org.example.demo.WalletError.WalletIssue.NotEnoughPoints
import org.example.demo.WalletError.WalletIssue.PassNotFound
import java.util.*

private val passNames = listOf(
    "Tesco - ClubCard",
    "Sainsbury - Nectar",
    "M&S - Sparks",
    "Waitrose - myWaitrose"
)

data class PassBuilder(
    val id: UUID = UUID.randomUUID(),
    val passName: String = passNames.random(),
    val passHolder: String = "John Doe",
    val points: Int = 0
) {
    fun build(): Pass = Pass(id, passName, passHolder, points)
}

data class WalletBuilder(
    val id: UUID = UUID.randomUUID(),
    val walletHolder: String = "John Doe",
    val passes: List<Pass> = emptyList(),
) {
    fun build(): Wallet = Wallet(id, walletHolder, passes)
}

private val walletIssues = listOf(
    PassNotFound(UUID.randomUUID()),
    NotEnoughPoints(UUID.randomUUID(), 0, 0)
)

class WalletErrorBuilder private constructor(
    private val passId: UUID,
    private val error: String,
    private val data: WalletIssue
) {
    companion object {
        fun ofPass(pass: Pass): WalletErrorBuilder =
            WalletErrorBuilder(pass.id, "some-error", randomIssue(pass.id))

        private fun randomIssue(passId: UUID): WalletIssue =
            walletIssues.random().let {
                when (it) {
                    is NotEnoughPoints -> it.copy(passId = passId)
                    is PassNotFound -> it.copy(id = passId)
            }
        }
    }

    fun build(): WalletError = WalletError(error, data)

    fun withNotEnoughPointsIssue(debitAmount: Int, balance: Int): WalletErrorBuilder =
        copy(error = "Insufficient balance.").withIssue(NotEnoughPoints(passId, debitAmount, balance))

    fun withPassNotFoundIssue(): WalletErrorBuilder =
        copy(error = "Pass not found.").withIssue(PassNotFound(passId))

    private fun withIssue(issue: WalletIssue): WalletErrorBuilder = copy(data = issue)

    private fun copy(passId: UUID = this.passId, error: String = this.error, data: WalletIssue = this.data): WalletErrorBuilder =
        WalletErrorBuilder(passId, error, data)
}

