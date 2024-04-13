package no.uio.ifi.in2000.election.oblig2.data.alpacas

import android.util.Log
import no.uio.ifi.in2000.election.oblig2.data.votes.VotesRepository
import no.uio.ifi.in2000.election.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.election.oblig2.model.votes.District
import no.uio.ifi.in2000.election.oblig2.model.votes.DistrictVotes
import javax.inject.Inject

interface AlpacaPartiesRepository { // suspend functions used for asynchronicity in these methods
    suspend fun getParties(): List<PartyInfo>?
    suspend fun getPartyInfo(partyID: String): PartyInfo?

    suspend fun getVoteList(district: District): Map<String, Int>? // combine party name with votes
}

class NetworkAlpacaPartiesRepository @Inject constructor(
    private val alpacaPartiesDataSource: AlpacaPartiesDataSource, // module and hilt takes care of construction
    private val votesRepository: VotesRepository
) : AlpacaPartiesRepository {
    private var parties : List<PartyInfo>? = null // keeping/caching parties when fetched
    private fun getPartyName(searchId: String): String {
        return parties?.find { it.id == searchId }?.name ?: "" // return empty string, maybe not optimal
    }

    override suspend fun getParties(): List<PartyInfo>? {
        if(parties == null){
            return try {
                parties = alpacaPartiesDataSource.fetchParties()
                Log.d("NetworkAlpacaPartiesRepository", "Fetched parties: $parties")
                parties
            } catch (e: Exception) {
                Log.e("NetworkAlpacaPartiesRepository", "Error fetching parties", e)
                return parties
            }
        }
        return null
    }

    override suspend fun getPartyInfo(partyID: String) : PartyInfo {
        if (parties == null){
            getParties()
        }

        val party : PartyInfo? = parties?.find { it.id == partyID }

        return party ?: throw IllegalArgumentException("Party with ID $partyID not found.")
    }

    override suspend fun getVoteList(district: District): Map<String, Int> {
        Log.d("AlpacaPartiesRepository", "entered getVoteList($district)")
        val districtVotes: List<DistrictVotes> = votesRepository.getDistrictVotes(district)
        Log.d("AlpacaPartiesRepository", "got districtVotes: $districtVotes")
        val voteList = mutableMapOf<String, Int>()
        districtVotes.forEach {// TODO: forEach
            voteList[getPartyName(it.alpacaPartyId)] = it.numberOfVotesForParty
        }
        return voteList
    }
}
