package org.example.demo

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
