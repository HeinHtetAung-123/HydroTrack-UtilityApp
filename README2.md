# HydroTrack Utility App

HydroTrack is a simple Android utility-style application built using Kotlin, Jetpack Compose, and Material Design 3. The app helps users track their daily water intake and view their hydration progress at a glance.

The purpose of HydroTrack is to provide focused, quick, and useful information for a daily-life activity. Instead of including many unrelated features, the app focuses on one clear utility purpose: helping users monitor how much water they have consumed compared with their daily hydration goal.

## App Purpose

HydroTrack is designed as an at-a-glance hydration tracker. The main screen gives users immediate information about their current intake, daily goal, progress percentage, remaining amount, and a progress-based message.

The app uses litres as the main display unit. Users can add water using quick-add buttons or by entering a custom amount. If the default input unit is millilitres, the app converts the entered value into litres before updating the dashboard. If the default input unit is litres, the entered value is used directly.

## Core Features

### Intake Dashboard

The Utility screen displays the user’s hydration progress in a clear dashboard format.

The dashboard includes:

* Current water intake
* Daily water goal
* Progress percentage
* Linear progress bar
* Remaining amount
* Progress-based message

The progress message changes depending on the user’s progress. For example, the app encourages the user when they are still working toward the goal and congratulates them when the goal is completed.

### Add Water Intake

Users can add water intake in two ways:

* Quick-add buttons
* Custom amount input

The quick-add buttons provide fast entry for common amounts such as 250ml, 500ml, and 1L. The custom amount input allows users to enter an amount that is not shown on the quick-add buttons.

When the user adds water, the dashboard updates immediately. This includes the current intake, progress percentage, progress bar, remaining amount, and progress message.

### Reset Intake

The Utility screen also includes a reset button. This allows the user to reset the current daily intake back to 0.00L.

### Settings Screen

The Settings screen controls important parts of the Utility screen.

Users can adjust:

* Daily hydration goal
* Custom daily goal
* Quick-add button amounts
* Default input unit
* Message style

The settings are not persistent, so they only apply while the app is running. This matches the assignment requirement that settings do not need to be saved permanently.

### Daily Goal Settings

Users can choose from preset hydration goals such as:

* 1.50L
* 2.00L
* 2.50L
* 3.00L

Users can also enter a custom goal in litres.

Changing the daily goal updates the dashboard immediately. The app recalculates the progress percentage, progress bar, remaining amount, and progress message based on the new goal.

### Quick-Add Button Settings

Users can customise the three quick-add buttons shown on the Utility screen.

For example, the default buttons may be:

* 250ml
* 500ml
* 1L

The user can change these to other values such as:

* 300ml
* 600ml
* 750ml

This makes the app more flexible because users can adjust the quick-add buttons to match the bottle, cup, or glass sizes they commonly use.

### Default Input Unit

Users can choose the default unit for custom water input:

* Millilitres
* Litres

The app always displays the final intake in litres. If the user selects millilitres and enters 500, the app converts it to 0.50L. If the user selects litres and enters 0.5, the app uses 0.50L directly.

### Message Style

Users can choose between two message styles:

* Simple
* Motivational

Simple messages provide short progress updates. Motivational messages provide more encouraging feedback. This setting changes how the progress message appears on the Utility screen.

### Weather Hydration Tip

HydroTrack includes a weather-based hydration suggestion using Retrofit and the Open-Meteo API. The app fetches the current temperature and displays a hydration tip based on the weather conditions.

For example, if the weather is warm, the app reminds the user to hydrate regularly. If the weather data cannot be loaded, the app displays a fallback hydration message.

This feature adds networking functionality while still supporting the main purpose of the app.

## Technical Implementation

HydroTrack uses modern Android development tools and practices, including:

* Kotlin
* Jetpack Compose
* Material Design 3
* ViewModel
* StateFlow
* Repository pattern
* Manual dependency injection
* Retrofit
* Open-Meteo Weather API

## Project Structure

```text
app/src/main/java/au/edu/jcu/cp3406_cp5307_utilityappstartertemplate/

model/
  HydroTrackUiState.kt
  InputUnit.kt
  MessageStyle.kt

viewmodel/
  HydroTrackViewModel.kt

data/
  WeatherApi.kt
  WeatherApiClient.kt
  WeatherRepository.kt
  WeatherResponse.kt

di/
  AppContainer.kt

ui/screens/
  UtilityScreen.kt
  SettingsScreen.kt
  HydroTrackFormatters.kt

ui/theme/
  Color.kt
  Theme.kt
  Type.kt

MainActivity.kt
```

## Architecture Overview

HydroTrack separates the app into clear layers.

### Model Layer

The model package contains the state and option types used by the app.

`HydroTrackUiState` stores the screen state, including the current intake, daily goal, quick-add amounts, selected input unit, message style, custom input values, and weather tip state.

`InputUnit` defines whether the custom input is treated as millilitres or litres.

`MessageStyle` defines whether the app uses simple or motivational progress messages.

### ViewModel Layer

`HydroTrackViewModel` manages the main app logic and state changes.

It handles:

* Adding water intake
* Converting millilitres to litres
* Resetting daily intake
* Updating the daily goal
* Applying a custom daily goal
* Updating the default input unit
* Updating message style
* Updating quick-add button values
* Loading the weather hydration tip

Using a ViewModel helps separate business logic from the UI and supports better Android lifecycle management.

### Data Layer

The data package handles API-related logic.

`WeatherApi` defines the Retrofit API request.

`WeatherResponse` represents the response data returned by the API.

`WeatherApiClient` creates the Retrofit client.

`WeatherRepository` fetches weather data and converts it into a hydration suggestion.

This keeps networking code separate from the UI.

### Dependency Injection Layer

The `di` package contains `AppContainer`, which provides app dependencies such as the weather repository.

This is a simple manual dependency injection approach. It keeps dependency creation outside the ViewModel and makes the code easier to organise and maintain.

### UI Layer

The `ui/screens` package contains the main Compose screens.

`UtilityScreen` displays the main HydroTrack dashboard, weather tip, quick-add buttons, custom input field, and reset button.

`SettingsScreen` displays controls for the daily goal, quick-add buttons, input unit, and message style.

`HydroTrackFormatters` contains helper functions for formatting litres, quick-add amounts, and progress messages.

## Jetpack Compose and Material Design 3 Usage

HydroTrack uses Jetpack Compose to build a modular and responsive interface. The UI is built using composable functions and Material Design 3 components.

The app uses components such as:

* `Scaffold`
* `NavigationBar`
* `NavigationBarItem`
* `Card`
* `Text`
* `Button`
* `OutlinedButton`
* `OutlinedTextField`
* `LinearProgressIndicator`
* `Column`
* `Row`

Cards are used to group related information clearly. The dashboard focuses on at-a-glance information, while the settings screen uses clear sections for each group of preferences.

## API Used

HydroTrack uses the Open-Meteo Weather API.

Open-Meteo provides weather forecast data and can return current weather values such as temperature. The API does not require an API key for non-commercial use, which makes it suitable for this student project.

Base URL:

```text
https://api.open-meteo.com/
```

Endpoint used:

```text
v1/forecast
```

The app requests current temperature using the `temperature_2m` variable.

## How the Weather Tip Works

The app uses Retrofit to call the Open-Meteo API. The repository receives the weather response and checks the current temperature.

The app then displays a hydration suggestion such as:

* Warm weather reminder
* Steady hydration reminder
* General hydration reminder
* Fallback message if weather data cannot be loaded

This API feature is connected to the app’s purpose because temperature can affect how often users may need reminders to drink water.

## How to Run the App

1. Clone the GitHub repository.
2. Open the project in Android Studio.
3. Wait for Gradle sync to finish.
4. Select an emulator or connected Android device.
5. Click Run.
6. Use the Utility tab to track water intake.
7. Use the Settings tab to change goals, input unit, message style, and quick-add buttons.

## Version Control

Git and GitHub were used to manage the project. Commits were made regularly after meaningful development steps, such as adding the ViewModel, building the dashboard, adding settings, refactoring the UI, adding Retrofit, creating the data layer, and adding dependency injection.

This commit history shows continuous development progress throughout the project.

## Current Limitations

The app does not permanently save settings or water intake. This means the data resets when the app restarts. This matches the assignment requirement because persistent settings were not required.

The weather feature currently uses a fixed location for the API request. A future version could allow the user to choose a location or use device location with permission handling.

## Future Improvements

Possible future improvements include:

* Saving daily intake using DataStore or Room
* Adding reminder notifications
* Allowing users to choose their weather location
* Showing weekly hydration history
* Adding charts for past progress
* Improving error handling for invalid custom input
* Adding more detailed accessibility support

## Conclusion

HydroTrack is a focused utility app that provides clear at-a-glance hydration tracking. It uses Kotlin, Jetpack Compose, Material Design 3, ViewModel, Repository pattern, manual dependency injection, and Retrofit. The app meets the utility app purpose by keeping the user experience simple, useful, and focused on daily water intake.
