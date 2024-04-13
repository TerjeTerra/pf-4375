package no.uio.ifi.in2000.election.oblig2.model.votes

/**
 * Data class representing the outer JSON object containing
 * an array (list) of PartyVote objects
 */
data class AggregatedVotes (
    val parties: List<PartyVotes>
)