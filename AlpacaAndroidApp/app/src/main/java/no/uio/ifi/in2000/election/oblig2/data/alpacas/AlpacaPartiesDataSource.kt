package no.uio.ifi.in2000.election.oblig2.data.alpacas

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.election.oblig2.model.alpacas.Parties
import no.uio.ifi.in2000.election.oblig2.model.alpacas.PartyInfo
import java.io.IOException

// Hent og deserialiser respons fra endepunkt. Returnerer en liste av PartyInfo-objekter
class AlpacaPartiesDataSource(
    private val path: String =
        "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/obligatoriske-oppgaver/alpacaparties.json"
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchParties(): List<PartyInfo> {
        try {
            val response: HttpResponse = client.get(path)
            Log.d(
                "AlpacaPartiesDataSource",
                "fetchParties() HTTP status: ${response.status}"
            )

            if (response.status == HttpStatusCode.OK) {
                val parties: Parties = response.body()

                Log.d(
                    "AlpacaPartiesDataSource",
                    "fetchParties() returned a Parties object containing ${parties.parties.size} parties"
                )
                return parties.parties
            } else {
                Log.e(
                    "AlpacaPartiesDataSource",
                    "fetchParties() failed with status: ${response.status}"
                )
                throw IOException("HTTP status: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("AlpacaPartiesDataSource", "General error fetching parties", e)
            throw e
        }
    }
}
