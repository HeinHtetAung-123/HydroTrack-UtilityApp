package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.InputUnit
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel.HydroTrackViewModel

@Composable
fun UtilityScreen(viewModel: HydroTrackViewModel) {
    // Collects ViewModel state so the screen automatically updates when intake,
    // settings, or weather tip values change.
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

        // Main at-a-glance dashboard showing the user's hydration progress.
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

                // Progress bar is capped at 100% in the UiState to keep the visual display clean.
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

                // Message changes depending on progress and the selected message style.
                Text(
                    text = getProgressMessage(
                        percentage = uiState.progressPercentage,
                        messageStyle = uiState.messageStyle
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Networking feature: displays a hydration suggestion based on current weather data.
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Weather Hydration Tip",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = if (uiState.isWeatherTipLoading) {
                        "Loading weather hydration tip..."
                    } else {
                        uiState.weatherTip
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedButton(
                    onClick = { viewModel.loadWeatherTip() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Refresh Tip")
                }
            }
        }

        // Input section for adding water using preset amounts or a custom amount.
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

                // Quick-add buttons are controlled by the Settings screen.
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

                // Custom amount input uses the default unit selected in Settings.
                // Millilitre input is converted to litres in the ViewModel.
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