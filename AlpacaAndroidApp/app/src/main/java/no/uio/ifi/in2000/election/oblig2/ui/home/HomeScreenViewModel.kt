package no.uio.ifi.in2000.election.oblig2.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.election.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.election.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.election.oblig2.model.votes.District
import javax.inject.Inject

data class PartiesUiState(val parties: List<PartyInfo>?,
                          val partyError: String? = null)
data class VoteListUiState(val voteList: Map<String, Int>?,
    val voteError: String? = null)

data class DistrictUiState(val district: District?)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val alpacaPartiesRepository: AlpacaPartiesRepository
) : ViewModel() {

    private var _partiesUiState: MutableState<PartiesUiState> =
        mutableStateOf(PartiesUiState(parties = null))
    private var _voteListUiState: MutableState<VoteListUiState> =
        mutableStateOf(VoteListUiState(voteList = null))
    private var _districtUiState: MutableState<DistrictUiState> =
        mutableStateOf(DistrictUiState(district = null))
    val partiesUiState: PartiesUiState
        get() = _partiesUiState.value // to be able to pass the value to Screen

    val voteListUiState: VoteListUiState
        get() = _voteListUiState.value

    val districtUiState: DistrictUiState
        get() = _districtUiState.value


    init {
        viewModelScope.launch(Dispatchers.IO){
            try {
                loadParties()
                Log.d("HomeScreenViewModel", "try to load party")
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error")
                _partiesUiState.value = _partiesUiState.value.copy(
                    parties = null,
                    partyError = "Failed to load parties")
            }
        }
    }


    suspend fun loadParties() {
        val parties: List<PartyInfo>?
        try  {
            parties = alpacaPartiesRepository.getParties()
            _partiesUiState.value = partiesUiState.copy(parties = parties) // update UiState
        } catch(e: Exception) {
            Log.e("HomeScreenViewModel", "Error loading parties", e)
            _partiesUiState.value = _partiesUiState.value.copy(
                parties = null,
                partyError = "Failed to load parties")
        }
    }


    fun updateVotes() { //loading votes according to districtUiState
        val district = districtUiState.district
        if (district == null) {
            _voteListUiState.value = _voteListUiState.value.copy(
                voteError = "No district is chosen",
                voteList = null
            )
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val voteList = districtUiState.district?.let {
                        alpacaPartiesRepository.getVoteList(
                            it
                        )
                    }
                    _voteListUiState.value = _voteListUiState.value.copy(
                        voteError = if (voteList == null) "Failed to update votes" else null,
                        voteList = voteList
                    )

                } catch (e: Exception) {
                    _voteListUiState.value = _voteListUiState.value.copy(
                        voteError = "Failed to update votes",
                        voteList = null
                    )
                }
            }
        }
    }

    fun updateDistrict(newDistrict: District) {
        _districtUiState.value = _districtUiState.value.copy(district = newDistrict)
    }
}