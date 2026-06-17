package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel

import androidx.lifecycle.ViewModel
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.HydroTrackUiState
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.InputUnit
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.MessageStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HydroTrackViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HydroTrackUiState())
    val uiState: StateFlow<HydroTrackUiState> = _uiState.asStateFlow()

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

        val amountInLitres = when (state.defaultInputUnit) {
            InputUnit.MILLILITRES -> enteredAmount / 1000
            InputUnit.LITRES -> enteredAmount
        }

        addWater(amountInLitres)

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

        return when {
            amount >= 10 -> amount / 1000
            else -> amount
        }
    }
}