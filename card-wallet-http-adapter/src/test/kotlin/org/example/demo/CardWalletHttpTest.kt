package org.example.demo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.filter.DebuggingFilters
import org.junit.jupiter.api.Test

class CardWalletHttpTest: CardWalletContract() {
    override val cardWallet: CardWalletPort =
        CardWalletHttpClient(CardWalletWebController(cardWalletLogicWithInMemoryRepository())
            .withFilter(DebuggingFilters.PrintRequestAndResponse())
        )

    @Test
    fun `can handle http to get a wallet by id`() {
        val wallet = cardWallet.createWallet("John Doe")

        val foundWallet = cardWallet.getWalletById(wallet.id)

        assertThat(foundWallet, equalTo(wallet))
    }
}