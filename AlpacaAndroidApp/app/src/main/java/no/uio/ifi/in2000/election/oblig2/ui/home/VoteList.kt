package no.uio.ifi.in2000.election.oblig2.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import no.uio.ifi.in2000.election.oblig2.model.votes.District
import no.uio.ifi.in2000.election.oblig2.model.votes.getDistrict
import no.uio.ifi.in2000.election.oblig2.ui.theme.ElectionTheme

//  SOME DUMMY-VALUES for previewScreen
class PreviewViewModel : ViewModel()

val testData = mapOf(
    "AlpacaNorth" to 12, "AlpacaWest" to 34, "AlpacaEast" to 56, "AlpacaSouth" to 78
)
val testVoteListUiState: VoteListUiState = VoteListUiState(
    testData, voteError = null // other option: "Failed to update votes"
)

val testDistrictUiState: DistrictUiState = DistrictUiState(District.D2)

val options = getDistrict()
var districtChosen = false // for preview purpose: change to true manually
var selectedDistrict: District? = null // for preview: change to a District (enum) manually

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteListComponent(
    voteListUiState: VoteListUiState, district: DistrictUiState, viewModel: ViewModel
) {
    val submittedDistrict by remember { mutableStateOf(districtChosen) }

    val infoText = remember {
        mutableStateOf(
            if (submittedDistrict) {
                "Resultat fra distrikt ${district.district}"
            } else {
                "Velg et distrikt fra menyen for å se resultat"
            }
        )
    }

    //focusManager (probably not essential for main functionality)
    val focusManager = LocalFocusManager.current

    fun submitDistrict(district: District) {
        if (viewModel is HomeScreenViewModel) { // to keep this part from running in preview
            districtChosen = true // user has chosen a district
            viewModel.updateDistrict(district) // update District
            viewModel.updateVotes()
        }
        focusManager.clearFocus() // clear focus (not essential)
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = Color.DarkGray)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = infoText.value,
                        style = TextStyle(color = Color.White, fontSize = 18.sp)
                    )
                }
                Column(
                    modifier = Modifier.weight(0.4f)
                ) {
                    // React on tap/press on TextField to show menu
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = selectedDistrict?.name ?: "",
                            onValueChange = {},
                            label = { Text("Velg distrikt") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption.toString()) },
                                    onClick = {
                                        selectedDistrict = selectionOption
                                        expanded = false
                                        submitDistrict(selectionOption)
                                        infoText.value = "Resultat fra distrikt $selectionOption"
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
            }
            // results
            if (voteListUiState.voteList != null) {
                if (voteListUiState.voteError == "Failed to update votes") {
                    PrintRow("Stemmedata er ikke tilgjengelig.", "", bold = true)
                } else {
                    PrintRow("Parti", "Ant. stemmer", bold = true) // header
                    voteListUiState.voteList.map {//now using map (not forEach)
                        PrintRow(it.key, it.value.toString())
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun VoteListPreview() {
    ElectionTheme {
        VoteListComponent(
            testVoteListUiState, testDistrictUiState, viewModel = PreviewViewModel()
        )
    }
}

@Composable
fun PrintRow(left: String, right: String, modifier: Modifier = Modifier, bold: Boolean = false) {
    val leftText = if (bold) {
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(left)
            }
        }
    } else {
        buildAnnotatedString { append(left) }
    }

    val rightText = if (bold) {
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(right)
            }
        }
    } else {
        buildAnnotatedString { append(right) }
    }
    Row(
        modifier
            .background(color = Color.LightGray)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start, modifier = Modifier.weight(0.5f)
        ) {
            Text(leftText)
        }
        //Spacer(modifier = Modifier.weight(0.1f))
        Column(
            horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.5f)
        ) {
            Text(rightText)
        }
    }
}