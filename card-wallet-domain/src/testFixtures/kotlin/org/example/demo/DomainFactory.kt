package org.example.demo

/**
 * Use this to refer to domain implementation with in-memory database, easy for testing.
 */
fun inMemoryCardWallet(): CardWalletPort = CardWalletLogic(repo = InMemoryCardWalletRepository())

private val passNames = listOf(
    "Tesco - ClubCard",
    "Sainsbury - Nectar",
    "M&S - Sparks",
    "Waitrose - myWaitrose"
)

fun aPass(id: PassId = PassId.random(), points: Int = 0) =
    Pass(id, passNames.random(), "John Doe", points)
