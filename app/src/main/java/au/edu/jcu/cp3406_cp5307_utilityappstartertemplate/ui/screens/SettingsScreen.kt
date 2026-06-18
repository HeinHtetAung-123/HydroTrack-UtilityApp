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
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.MessageStyle
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel.HydroTrackViewModel

@Composable
fun SettingsScreen(viewModel: HydroTrackViewModel) {
    // Collects the same shared state used by UtilityScreen.
    // This allows settings changes to update the main dashboard immediately.
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

        // Daily goal settings control the target shown in the Utility dashboard.
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

                // Preset goal buttons provide quick configuration for common hydration targets.
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

                // Custom goal input lets users set a goal that is not part of the presets.
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

        // Quick-add settings control the three quick-add buttons on the Utility screen.
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

                // Applies the edited quick-add values to the Utility screen buttons.
                Button(
                    onClick = { viewModel.applyQuickAddAmounts() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Quick Add Buttons")
                }
            }
        }

        // Default input unit controls how the custom amount field on the Utility screen is interpreted.
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

        // Message style changes the tone of the progress message in the dashboard.
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