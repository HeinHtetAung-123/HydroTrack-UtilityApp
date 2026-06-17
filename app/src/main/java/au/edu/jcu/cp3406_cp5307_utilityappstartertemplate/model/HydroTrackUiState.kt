package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model

data class HydroTrackUiState(
    val currentIntakeLitres: Double = 0.0,
    val dailyGoalLitres: Double = 2.0,
    val quickAddAmountsLitres: List<Double> = listOf(0.25, 0.5, 1.0),
    val defaultInputUnit: InputUnit = InputUnit.MILLILITRES,
    val messageStyle: MessageStyle = MessageStyle.MOTIVATIONAL,
    val customAmountText: String = "",
    val customGoalText: String = "",
    val quickAddOneText: String = "250",
    val quickAddTwoText: String = "500",
    val quickAddThreeText: String = "1"
) {
    val progress: Float
        get() = if (dailyGoalLitres > 0) {
            (currentIntakeLitres / dailyGoalLitres).toFloat().coerceAtMost(1f)
        } else {
            0f
        }

    val progressPercentage: Int
        get() = if (dailyGoalLitres > 0) {
            ((currentIntakeLitres / dailyGoalLitres) * 100).toInt()
        } else {
            0
        }

    val remainingLitres: Double
        get() = (dailyGoalLitres - currentIntakeLitres).coerceAtLeast(0.0)

    val isGoalCompleted: Boolean
        get() = currentIntakeLitres >= dailyGoalLitres
}