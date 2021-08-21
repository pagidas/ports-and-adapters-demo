package org.example.demo

import org.http4k.filter.DebuggingFilters

class CardWalletHttpTest: CardWalletContract() {
    override val cardWallet: CardWalletPort =
        CardWalletHttpClient(CardWalletWebController(cardWalletLogicWithInMemoryRepository())
            .withFilter(DebuggingFilters.PrintRequestAndResponse())
        )
}