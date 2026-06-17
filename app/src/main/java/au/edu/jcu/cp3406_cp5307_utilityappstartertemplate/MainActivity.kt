package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate

import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel.HydroTrackViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme.CP3406_CP5603UtilityAppStarterTemplateTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.collectAsState
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.MessageStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CP3406_CP5603UtilityAppStarterTemplateTheme {
                UtilityApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    CP3406_CP5603UtilityAppStarterTemplateTheme {
        UtilityApp()
    }
}

@Composable
fun UtilityApp() {
    val hydroTrackViewModel: HydroTrackViewModel = viewModel()
    var selectedTab by remember { mutableStateOf("Utility") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                    label = { Text("Utility") },
                    selected = selectedTab == "Utility",
                    onClick = { selectedTab = "Utility" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Utility" -> UtilityScreen(viewModel = hydroTrackViewModel)
                "Settings" -> SettingsScreen(viewModel = hydroTrackViewModel)
            }
        }
    }
}

@Composable
fun UtilityScreen(viewModel: HydroTrackViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "HydroTrack",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Track your daily water intake at a glance.",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Today’s Intake",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${formatLitres(uiState.currentIntakeLitres)} / ${formatLitres(uiState.dailyGoalLitres)}",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "${uiState.progressPercentage}% completed",
                    style = MaterialTheme.typography.bodyLarge
                )

                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = if (uiState.isGoalCompleted) {
                        "Goal completed"
                    } else {
                        "${formatLitres(uiState.remainingLitres)} remaining"
                    },
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = getProgressMessage(
                        percentage = uiState.progressPercentage,
                        messageStyle = uiState.messageStyle
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
@Composable
fun SettingsScreen(viewModel: HydroTrackViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp), Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings Screen", style = MaterialTheme.typography.headlineMedium)
        Text("This is where you can add toggles or preferences.")
    }
}
fun formatLitres(value: Double): String {
    return String.format("%.2fL", value)
}

fun getProgressMessage(
    percentage: Int,
    messageStyle: MessageStyle
): String {
    val simpleMessage = when {
        percentage <= 0 -> "Start your hydration goal for today."
        percentage < 25 -> "Good start."
        percentage < 50 -> "Steady progress."
        percentage < 75 -> "More than halfway there."
        percentage < 100 -> "Almost there."
        else -> "Goal completed."
    }

    val motivationalMessage = when {
        percentage <= 0 -> "Start your hydration goal for today."
        percentage < 25 -> "Good start. Keep going."
        percentage < 50 -> "You are making steady progress."
        percentage < 75 -> "Nice work. You are more than halfway there."
        percentage < 100 -> "Almost there. Just a little more to go."
        else -> "Well done! You completed your hydration goal."
    }

    return when (messageStyle) {
        MessageStyle.SIMPLE -> simpleMessage
        MessageStyle.MOTIVATIONAL -> motivationalMessage
    }
}