package no.uio.ifi.in2000.election.oblig2.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.election.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.election.oblig2.ui.toColor

@Composable
fun HomeScreen(
    onPartyClick: (String) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    // using hiltViewModel in order to be consequent in app
    // observing UiState changes of the following data
    val partiesUiState = homeScreenViewModel.partiesUiState
    val parties = partiesUiState.parties // might be null if not loading succeeded
    val voteListUiState = homeScreenViewModel.voteListUiState
    val district = homeScreenViewModel.districtUiState
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = partiesUiState.partyError, block = {
        val partyError = partiesUiState.partyError
        if (partyError != null) {
            scope.launch {
                val result = snackBarHostState.showSnackbar(
                    message = partyError, actionLabel = "Last inn en gang til."
                )
                if (result == SnackbarResult.ActionPerformed) {
                    homeScreenViewModel.loadParties()
                }
            }
        }
    }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        if (parties == null) {
            if (partiesUiState.partyError != null) {
                Text(partiesUiState.partyError)
            }
        } else {
            BoxWithConstraints(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                // calculation of padding to be used in landscape mode
                val screenWidth = maxWidth
                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                // Set padding percentage if in landscape mode, else use default padding
                val paddingPercent = if (isLandscape) 0.2f else 0f
                val paddingHorizontal = screenWidth * paddingPercent

                Column(
                    modifier = Modifier.padding(horizontal = paddingHorizontal)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Valget i Alpaccaland 2024",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    VoteListComponent(
                        voteListUiState,
                        district,
                        homeScreenViewModel
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(parties) { partyInfo ->
                            PartyInfoCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                partyInfo = partyInfo,
                                onClick = { onPartyClick(partyInfo.id) } // send party id as parameter
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun PartyInfoCard(modifier: Modifier, partyInfo: PartyInfo, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp)
    ) {
        val colorStripHeight = 18
        Column {
            // colored strip with color of party, within a column in the Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(colorStripHeight.dp)
                    .background(partyInfo.color.toColor())
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                // Arrange the children such that they are spaced evenly across the main axis
                horizontalArrangement = Arrangement.SpaceEvenly,
                // The children of the Row are centered vertically
                verticalAlignment = Alignment.CenterVertically

            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // PICTURE
                    AsyncImage(
                        modifier = Modifier
                            .size(164.dp),
                        model = partyInfo.img,
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.weight(0.5f)) // Flexible space above to center text
                    // NAME of PARTY
                    Text(
                        text = partyInfo.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // NAME and TITLE of leader
                    Text(text = partyInfo.leader)
                    Text(
                        text = "partileder",
                        style = TextStyle(
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(Modifier.weight(0.5f)) // Flexible space below to center text
                }
            }

            Box( // colored strip at bottom
                modifier = Modifier
                    .fillMaxWidth()
                    .height(colorStripHeight.dp)
                    .background(partyInfo.color.toColor())
            )
        }
    }
}
