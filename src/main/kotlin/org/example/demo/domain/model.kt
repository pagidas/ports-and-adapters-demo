package org.example.demo.domain

import java.util.*

data class Wallet(val id: WalletId, val walletHolder: String)
data class WalletId(val value: UUID) {
    companion object {
        fun random() = WalletId(UUID.randomUUID())
    }
}
