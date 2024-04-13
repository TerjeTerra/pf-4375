package no.uio.ifi.in2000.election.oblig2.ui.party

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.election.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.election.oblig2.model.alpacas.PartyInfo
import javax.inject.Inject

data class PartyInfoUiState(
    val partyInfo: PartyInfo? = null, val partyError: String? = null
)

@HiltViewModel // using specifically HiltViewModel
class PartyViewModel @Inject constructor( // see data.di.AlpacaPartiesRepositoryModule (dep.injection)
    private val savedStateHandle: SavedStateHandle,
    private val alpacaPartiesRepository: AlpacaPartiesRepository // Dependency injection here
) : ViewModel() {

    private var _partyInfoUiState =
        MutableStateFlow(PartyInfoUiState(PartyInfo("", "", "", "", "", "")))
    val partyInfoUiState = _partyInfoUiState.asStateFlow()

    init {
        loadPartyInfo()
    }

    fun loadPartyInfo() {
        val partyId: String = savedStateHandle["partyId"]
            ?: "1" //Get partyId from savedStateHandle, or use 1 as default
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val partyInfo = alpacaPartiesRepository.getPartyInfo(partyId)
                withContext(Dispatchers.Main) {// update UiState in Main thread
                    _partyInfoUiState.update { it.copy(partyInfo = partyInfo, partyError = null) }
                }
            } catch (e: Exception) {
                Log.e("PartyViewModel", "Error loading parties", e)
                _partyInfoUiState.update {
                    it.copy(
                        partyInfo = null, partyError = "Failed to load parties"
                    )
                }

            }
        }
    }
}
