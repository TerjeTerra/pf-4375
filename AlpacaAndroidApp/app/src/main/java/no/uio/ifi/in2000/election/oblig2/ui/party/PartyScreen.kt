package no.uio.ifi.in2000.election.oblig2.ui.party

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.election.oblig2.ui.toColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyScreen(
    onBarClick: () -> Unit,
    partyViewModel: PartyViewModel = hiltViewModel(),
) {
    val modifier = Modifier.padding(horizontal = 20.dp)
    val scrollState = rememberScrollState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val state by partyViewModel.partyInfoUiState.collectAsState() // observing UiState changes
    val partyInfo = state.partyInfo
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.partyError, block = {
        val partyError = state.partyError
        if (partyError != null) {
            scope.launch {
                val result =
                    snackBarHostState.showSnackbar(message = partyError, actionLabel = "Try again")
                if (result == SnackbarResult.ActionPerformed) {
                    partyViewModel.loadPartyInfo()
                }
            }
        }
    })

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text(
                    "Tilbake",
                    maxLines = 1,
                )
            }, navigationIcon = {
                IconButton(onClick = { onBarClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        if (partyInfo == null) {
            val partyError = state.partyError
            if (partyError == null) {
                Text("Loading...")
            } else {
                Text(partyError)
            }
        } else {
            Column(
                modifier
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val colorStripHeight = 24// 18 ok?
                // Using remember to cache computed values in order not to recreate on every recomposition
                val partyColor = remember(partyInfo.color) {
                    partyInfo.color.toColor()
                }
                val partyImage = remember(partyInfo.img) {
                    partyInfo.img
                }
                val partyName = remember(partyInfo.name) {
                    partyInfo.name
                }

                // NAME OF PARTY
                Text(
                    text = partyName, style = MaterialTheme.typography.titleLarge
                )

                // PICTURE
                Box(
                    modifier = Modifier
                        .height(224.dp) // visible height for the image
                        .fillMaxWidth()
                        .clip(RectangleShape) // only show content within these bounds
                ) {
                    AsyncImage(
                        model = partyImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop, // crop to fill the bounds, cut top/bottom
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(336.dp)
                            .align(Alignment.Center)
                    )
                }

                // NAME and TITLE of leader
                Text(text = partyInfo.leader)
                Text(
                    text = "partileder", style = TextStyle(
                        fontStyle = FontStyle.Italic, fontSize = 12.sp
                    )
                )

                // COLOR of party
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(colorStripHeight.dp)
                        .background(partyColor)
                )


                // DESCRIPTION of leader
                Text(text = partyInfo.description)
            }
        }
    }
}