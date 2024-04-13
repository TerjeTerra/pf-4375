package no.uio.ifi.in2000.election.oblig2.data.votes

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.election.oblig2.model.votes.Vote
import java.io.IOException

class IndividualVotesDataSource (
    private val path1: String = "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/obligatoriske-oppgaver/district1.json",
    private val path2: String = "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/obligatoriske-oppgaver/district2.json"
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchIndividualVotes(apiNumber: String): List<Vote> {
        val path: String = when (apiNumber) {
            "1" -> path1
            "2" -> path2
            else -> throw IOException("api number $apiNumber does not refer to api")
        }
        try {
            val response: HttpResponse = client.get(path)
            Log.d(
                "IndividualVotesDataSource",
                "fetchIndividualVotes() HTTP status: ${response.status}"
            )

            if (response.status == HttpStatusCode.OK) {
                val votes: List<Vote> = response.body()

                Log.d(
                    "IndividualVotesDataSource",
                    "fetchIndividualVotes() returned a Votes object containing ${votes.size} votes"
                )
                return votes
            } else {
                Log.e(
                    "IndividualVotesDataSource",
                    "fetchAggregatedVotes() failed with status: ${response.status}"
                )
                throw IOException("HTTP status: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("IndividualVotesDataSource", "General error fetching Votes", e)
            throw e
        }
    }
}