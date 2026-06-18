package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model

// Holds all UI-related state for HydroTrack in one immutable data class.
// The ViewModel updates this state, and Compose observes it to refresh the screens.
data class HydroTrackUiState(
    val currentIntakeLitres: Double = 0.0,
    val dailyGoalLitres: Double = 2.0,
    val quickAddAmountsLitres: List<Double> = listOf(0.25, 0.5, 1.0),
    val defaultInputUnit: InputUnit = InputUnit.MILLILITRES,
    val messageStyle: MessageStyle = MessageStyle.MOTIVATIONAL,

    // Text field values are stored in state so the UI and ViewModel stay synchronised.
    val customAmountText: String = "",
    val customGoalText: String = "",
    val quickAddOneText: String = "250",
    val quickAddTwoText: String = "500",
    val quickAddThreeText: String = "1",

    // Weather tip state is updated after the Retrofit API request completes.
    val weatherTip: String = "Loading weather hydration tip...",
    val isWeatherTipLoading: Boolean = true
) {
    // Progress is capped at 1f so the progress bar does not visually exceed 100%.
    val progress: Float
        get() = if (dailyGoalLitres > 0) {
            (currentIntakeLitres / dailyGoalLitres).toFloat().coerceAtMost(1f)
        } else {
            0f
        }

    // Percentage can go over 100 so the user can still see when they exceed the goal.
    val progressPercentage: Int
        get() = if (dailyGoalLitres > 0) {
            ((currentIntakeLitres / dailyGoalLitres) * 100).toInt()
        } else {
            0
        }

    // Remaining amount never goes below zero, even if the user passes their goal.
    val remainingLitres: Double
        get() = (dailyGoalLitres - currentIntakeLitres).coerceAtLeast(0.0)

    val isGoalCompleted: Boolean
        get() = currentIntakeLitres >= dailyGoalLitres
}