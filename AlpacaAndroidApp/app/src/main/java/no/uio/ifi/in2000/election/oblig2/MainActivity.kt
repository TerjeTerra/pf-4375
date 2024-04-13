package no.uio.ifi.in2000.election.oblig2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.uio.ifi.in2000.election.oblig2.ui.home.HomeScreen
import no.uio.ifi.in2000.election.oblig2.ui.party.PartyScreen
import no.uio.ifi.in2000.election.oblig2.ui.party.PartyViewModel
import no.uio.ifi.in2000.election.oblig2.ui.theme.ElectionTheme
import androidx.hilt.navigation.compose.hiltViewModel  // Using Hilt with Compose
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.election.oblig2.ui.home.HomeScreenViewModel

@AndroidEntryPoint // (source: https://developer.android.com/training/dependency-injection/hilt-android)
class MainActivity : ComponentActivity() {
    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElectionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onPartyClick = { navController.navigate("party/$it") },
                                homeScreenViewModel
                            )
                        }
                        composable("party/{partyId}",
                            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val partyViewModel: PartyViewModel = hiltViewModel(backStackEntry)
                            // passing on partyId through the hiltViewModel to PartyScreen
                            // that reflects state of PartyViewModel instanced with "party/{partyId}

                            PartyScreen(
                                onBarClick = {
                                    // Solution suggested by UiO GPT-4 when asked to prevent failure
                                    // with app popping backstack too far on quick multiple clicks.
                                    // Instead use popUpTo navigation structure in order not to be
                                    // making a new instance of home screen.
                                    navController.navigate("home") {
                                        launchSingleTop = true // if home on top of BackStack, no navigation
                                    }
                                },
                                partyViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}