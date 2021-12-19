package org.example.demo

import java.util.*

/**
 * Use this to refer to domain implementation with in-memory database, easy for testing.
 */
object InMemoryCardWallet {
    operator fun invoke(): CardWalletPort = CardWalletLogic(repo = FakeCardWalletRepository())
}

private val passNames = listOf(
    "Tesco - ClubCard",
    "Sainsbury - Nectar",
    "M&S - Sparks",
    "Waitrose - myWaitrose"
)

fun aPass(id: UUID = UUID.randomUUID(), points: Int = 0) =
    Pass(id, passNames.random(), "John Doe", points)
