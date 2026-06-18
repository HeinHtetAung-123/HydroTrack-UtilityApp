package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.screens

import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.model.MessageStyle

// Formats litre values consistently across the app.
fun formatLitres(value: Double): String {
    return String.format("%.2fL", value)
}

// Shows quick-add amounts in a user-friendly way.
// Amounts below 1 litre are displayed as millilitres.
fun formatQuickAddAmount(valueLitres: Double): String {
    return if (valueLitres < 1.0) {
        "${(valueLitres * 1000).toInt()}ml"
    } else {
        "${String.format("%.2f", valueLitres)}L"
    }
}

// Returns the dashboard progress message based on completion percentage
// and the message style selected in Settings.
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