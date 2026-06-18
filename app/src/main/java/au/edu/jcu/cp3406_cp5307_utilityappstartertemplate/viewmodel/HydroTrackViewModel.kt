package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.di.AppContainer
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.HydroTrackUiState
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.InputUnit
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.MessageStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HydroTrackViewModel : ViewModel() {

    // Repository is provided through the app container to keep dependency creation
    // separate from the ViewModel.
    private val weatherRepository = AppContainer.weatherRepository

    // Mutable state is kept private so only the ViewModel can change app data.
    private val _uiState = MutableStateFlow(HydroTrackUiState())

    // The UI observes this read-only StateFlow and recomposes when values change.
    val uiState: StateFlow<HydroTrackUiState> = _uiState.asStateFlow()

    init {
        // Loads the weather-based hydration tip when the ViewModel is first created.
        loadWeatherTip()
    }

    fun addWater(amountLitres: Double) {
        if (amountLitres <= 0) return

        _uiState.update { currentState ->
            currentState.copy(
                currentIntakeLitres = currentState.currentIntakeLitres + amountLitres
            )
        }
    }

    fun addCustomAmount() {
        val state = _uiState.value
        val enteredAmount = state.customAmountText.toDoubleOrNull() ?: return

        // The app always stores and displays intake in litres.
        // Millilitre input is converted before being added to the total.
        val amountInLitres = when (state.defaultInputUnit) {
            InputUnit.MILLILITRES -> enteredAmount / 1000
            InputUnit.LITRES -> enteredAmount
        }

        addWater(amountInLitres)

        // Clears the input field after a valid custom amount is added.
        _uiState.update {
            it.copy(customAmountText = "")
        }
    }

    fun updateCustomAmountText(value: String) {
        _uiState.update {
            it.copy(customAmountText = value)
        }
    }

    fun resetIntake() {
        _uiState.update {
            it.copy(currentIntakeLitres = 0.0)
        }
    }

    fun updateDailyGoal(goalLitres: Double) {
        if (goalLitres <= 0) return

        _uiState.update {
            it.copy(dailyGoalLitres = goalLitres)
        }
    }

    fun updateCustomGoalText(value: String) {
        _uiState.update {
            it.copy(customGoalText = value)
        }
    }

    fun applyCustomGoal() {
        val customGoal = _uiState.value.customGoalText.toDoubleOrNull() ?: return

        if (customGoal <= 0) return

        _uiState.update {
            it.copy(
                dailyGoalLitres = customGoal,
                customGoalText = ""
            )
        }
    }

    fun updateDefaultInputUnit(unit: InputUnit) {
        _uiState.update {
            it.copy(defaultInputUnit = unit)
        }
    }

    fun updateMessageStyle(style: MessageStyle) {
        _uiState.update {
            it.copy(messageStyle = style)
        }
    }

    fun updateQuickAddAmount(index: Int, value: String) {
        _uiState.update { currentState ->
            when (index) {
                0 -> currentState.copy(quickAddOneText = value)
                1 -> currentState.copy(quickAddTwoText = value)
                2 -> currentState.copy(quickAddThreeText = value)
                else -> currentState
            }
        }
    }

    fun applyQuickAddAmounts() {
        val state = _uiState.value

        val amountOne = convertQuickAddInputToLitres(state.quickAddOneText)
        val amountTwo = convertQuickAddInputToLitres(state.quickAddTwoText)
        val amountThree = convertQuickAddInputToLitres(state.quickAddThreeText)

        if (amountOne <= 0 || amountTwo <= 0 || amountThree <= 0) return

        _uiState.update {
            it.copy(
                quickAddAmountsLitres = listOf(amountOne, amountTwo, amountThree)
            )
        }
    }

    private fun convertQuickAddInputToLitres(value: String): Double {
        val amount = value.toDoubleOrNull() ?: return 0.0

        // Values 10 or above are treated as millilitres, such as 250 or 500.
        // Smaller values are treated as litres, such as 1 or 1.5.
        return when {
            amount >= 10 -> amount / 1000
            else -> amount
        }
    }

    fun loadWeatherTip() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isWeatherTipLoading = true)
            }

            // Network work is handled by the repository so the ViewModel stays focused
            // on state management rather than Retrofit implementation details.
            val tip = weatherRepository.getHydrationSuggestion()

            _uiState.update {
                it.copy(
                    weatherTip = tip,
                    isWeatherTipLoading = false
                )
            }
        }
    }
}