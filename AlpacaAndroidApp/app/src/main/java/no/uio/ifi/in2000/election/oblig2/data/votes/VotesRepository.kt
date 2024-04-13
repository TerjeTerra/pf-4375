package no.uio.ifi.in2000.election.oblig2.data.votes

import android.util.Log
import no.uio.ifi.in2000.election.oblig2.model.votes.District
import no.uio.ifi.in2000.election.oblig2.model.votes.DistrictVotes
import no.uio.ifi.in2000.election.oblig2.model.votes.PartyVotes
import javax.inject.Inject

interface VotesRepository {
    suspend fun getDistrictVotes(district: District): List<DistrictVotes> // suspend used for asynchronicity
}

class VotesRepositoryImpl @Inject constructor( // module and hilt takes care of construction
    private val individualVotesDataSource: IndividualVotesDataSource,
    private val aggregatedVotesDataSource: AggregatedVotesDataSource
) : VotesRepository {
    override suspend fun getDistrictVotes(district: District): List<DistrictVotes> {
        var aggregatedDistrictVotes: MutableList<DistrictVotes> = mutableListOf()

        // update aggregatedDistrictVotes based on format from Individual or Party Votes
        // using higher order functions, no loops
        fun updateFromIndividualVotes(voteList: Map<String, Int>) {
            aggregatedDistrictVotes = voteList.map { (id: String, count: Int) ->
                DistrictVotes(district, id, count)
            }.toMutableList()
        }

        fun updateFromPartyVotes(votes: List<PartyVotes>) {
            aggregatedDistrictVotes = votes.map { (partyId: String, votes: Int) ->
                DistrictVotes(district, partyId, votes)
            }.toMutableList()
        }

        when (district) {
            // fixed: now using higher order functions, not .forEach syntax, for data transformation
            District.D1 -> try {
                updateFromIndividualVotes(individualVotesDataSource.fetchIndividualVotes("1")
                    .groupBy { it.id }.mapValues { it.value.size })
                Log.d(
                    "VotesRepository",
                    "Fetched and converted from IndividualVotesDatasource: $aggregatedDistrictVotes"
                )
                return aggregatedDistrictVotes
            } catch (e: Exception) {
                Log.e(
                    "VotesRepository",
                    "Error getting districtVotes from IndividualVotesDatasource district D1",
                    e
                )
            }

            District.D2 -> try {
                updateFromIndividualVotes(individualVotesDataSource.fetchIndividualVotes("2")
                    .groupBy { it.id }.mapValues { it.value.size })
                Log.d(
                    "VotesRepository",
                    "Fetched and converted from IndividualVotesDatasource: $aggregatedDistrictVotes"
                )
                return aggregatedDistrictVotes
            } catch (e: Exception) {
                Log.e(
                    "VotesRepository",
                    "Error getting districtVotes from IndividualVotesDatasource district D2",
                    e
                )
            }

            District.D3 -> try {
                updateFromPartyVotes(aggregatedVotesDataSource.fetchAggregatedVotes())
                Log.d(
                    "VotesRepository",
                    "Fetched and converted from AggregatedVotesDatasource: $aggregatedDistrictVotes"
                )
                return aggregatedDistrictVotes
            } catch (e: Exception) {
                Log.e(
                    "VotesRepository",
                    "Error getting districtVotes from AggregatedVotesDatasource district D3",
                    e
                )
            }
        }
        return aggregatedDistrictVotes
    }
}
