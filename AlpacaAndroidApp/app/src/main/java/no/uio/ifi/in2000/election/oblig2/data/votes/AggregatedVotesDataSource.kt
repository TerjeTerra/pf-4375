package no.uio.ifi.in2000.election.oblig2.data.votes

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.election.oblig2.model.votes.AggregatedVotes
import no.uio.ifi.in2000.election.oblig2.model.votes.PartyVotes
import java.io.IOException

// Get data from cloud
class AggregatedVotesDataSource (
    private val path: String =
        "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/obligatoriske-oppgaver/district3.json"
) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchAggregatedVotes(): List<PartyVotes> {
        try {
            val response: HttpResponse = client.get(path)
            Log.d(
                "AggregatedVotesDataSource",
                "fetchAggregatedVotes() HTTP status: ${response.status}"
            )

            if (response.status == HttpStatusCode.OK) {
                val aggregatedVotes: AggregatedVotes = response.body()

                Log.d(
                    "AggregatedVotesDataSource",
                    "fetchAggregatedVotes() returned a Parties object containing " +
                            "${aggregatedVotes.parties.size} parties"
                )
                return aggregatedVotes.parties
            } else {
                Log.e(
                    "AggregatedVotesDataSource",
                    "fetchAggregatedVotes() failed with status: ${response.status}"
                )
                throw IOException("HTTP status: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("AggregatedVotesDataSource", "General error fetching AggregatedVotes", e)
            throw e
        }
    }
}

