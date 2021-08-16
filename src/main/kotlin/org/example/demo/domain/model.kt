package org.example.demo.domain

import java.util.*

typealias Passes = LinkedList<Pass>

data class Wallet(val id: WalletId, val walletHolder: String, val passes: Passes) {
    companion object {
        fun empty(id: WalletId, walletHolder: String) = Wallet(WalletId.random(), walletHolder, LinkedList())
    }
}
data class WalletId(val value: UUID) {
    companion object {
        fun random() = WalletId(UUID.randomUUID())
    }
}
data class Pass(val id: PassId, val passName: String, val passHolder: String)
data class PassId(val value: UUID) {
    companion object {
        fun random() = PassId(UUID.randomUUID())
    }
}
