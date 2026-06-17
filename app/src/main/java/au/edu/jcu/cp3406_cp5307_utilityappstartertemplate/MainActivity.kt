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
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.InputUnit
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
            .verticalScroll(rememberScrollState())
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

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Add Water",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.quickAddAmountsLitres.forEach { amount ->
                        Button(
                            onClick = { viewModel.addWater(amount) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+${formatQuickAddAmount(amount)}")
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.customAmountText,
                    onValueChange = { viewModel.updateCustomAmountText(it) },
                    label = {
                        Text(
                            text = if (uiState.defaultInputUnit == InputUnit.MILLILITRES) {
                                "Custom amount in ml"
                            } else {
                                "Custom amount in L"
                            }
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.addCustomAmount() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Custom Amount")
                }

                OutlinedButton(
                    onClick = { viewModel.resetIntake() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Today’s Intake")
                }
            }
        }
    }
}
@Composable
fun SettingsScreen(viewModel: HydroTrackViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Adjust your hydration goal and display preferences.",
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
                    text = "Daily Goal",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1.5, 2.0, 2.5, 3.0).forEach { goal ->
                        OutlinedButton(
                            onClick = { viewModel.updateDailyGoal(goal) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(formatLitres(goal))
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.customGoalText,
                    onValueChange = { viewModel.updateCustomGoalText(it) },
                    label = { Text("Custom goal in L") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.applyCustomGoal() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Custom Goal")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Quick Add Buttons",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Enter values in ml for amounts like 250 or 500. Enter litres for values like 1 or 1.5.",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.quickAddOneText,
                        onValueChange = { viewModel.updateQuickAddAmount(0, it) },
                        label = { Text("Button 1") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = uiState.quickAddTwoText,
                        onValueChange = { viewModel.updateQuickAddAmount(1, it) },
                        label = { Text("Button 2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = uiState.quickAddThreeText,
                        onValueChange = { viewModel.updateQuickAddAmount(2, it) },
                        label = { Text("Button 3") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = { viewModel.applyQuickAddAmounts() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Quick Add Buttons")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Default Input Unit",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.updateDefaultInputUnit(InputUnit.MILLILITRES) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ml")
                    }

                    Button(
                        onClick = { viewModel.updateDefaultInputUnit(InputUnit.LITRES) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("L")
                    }
                }

                Text(
                    text = "Current input unit: ${
                        if (uiState.defaultInputUnit == InputUnit.MILLILITRES) "ml" else "L"
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Message Style",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.updateMessageStyle(MessageStyle.SIMPLE) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Simple")
                    }

                    Button(
                        onClick = { viewModel.updateMessageStyle(MessageStyle.MOTIVATIONAL) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Motivational")
                    }
                }

                Text(
                    text = "Current style: ${
                        if (uiState.messageStyle == MessageStyle.SIMPLE) "Simple" else "Motivational"
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
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
fun formatQuickAddAmount(valueLitres: Double): String {
    return if (valueLitres < 1.0) {
        "${(valueLitres * 1000).toInt()}ml"
    } else {
        "${String.format("%.2f", valueLitres)}L"
    }
}