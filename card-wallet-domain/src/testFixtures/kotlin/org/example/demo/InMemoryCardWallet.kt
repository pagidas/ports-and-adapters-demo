package org.example.demo

/**
 * Use this to refer to domain implementation with in-memory database, easy for testing.
 */
object InMemoryCardWallet {
    operator fun invoke(): CardWalletPort = CardWalletLogic(storage = FakeCardWalletStorage())
}