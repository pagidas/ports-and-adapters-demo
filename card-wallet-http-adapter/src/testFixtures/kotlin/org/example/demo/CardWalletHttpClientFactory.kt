package org.example.demo

import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters

/**
 * This factory is responsible to construct the http client,
 * that knows how to talk to [CardWalletWebController].
 *
 * Currently, it is shared on testFixtures project, only used
 * for tests that need to drive the domain port through http.
 */
class CardWalletHttpClientFactory {
    companion object {
        fun ofUri(uri: String): CardWalletPort =
            CardWalletHttpClient(
                ClientFilters.SetBaseUriFrom(Uri.of(uri))
                    .then(JavaHttpClient()))
    }
}