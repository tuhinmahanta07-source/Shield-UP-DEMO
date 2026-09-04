package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.SleekBottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.CanvasNavy
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: ShieldUpViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ShieldUpApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ShieldUpApp(viewModel: ShieldUpViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val showOnboarding by viewModel.showOnboarding.collectAsState()

  // Handle system back button
  BackHandler(enabled = currentScreen != AppScreen.HOME) {
    viewModel.navigateTo(AppScreen.HOME)
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasNavy),
    containerColor = CanvasNavy,
    bottomBar = {
      SleekBottomNavBar(
        currentScreen = currentScreen,
        onNavigate = { screen -> viewModel.navigateTo(screen) }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(CanvasNavy)
    ) {
      Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
          AppScreen.HOME -> HomeScreen(viewModel = viewModel)
          AppScreen.STAGE_1 -> Stage1Screen(viewModel = viewModel)
          AppScreen.STAGE_2 -> Stage2Screen(viewModel = viewModel)
          AppScreen.STAGE_3 -> Stage3Screen(viewModel = viewModel)
          AppScreen.JOURNEY -> JourneyScreen(viewModel = viewModel)
          AppScreen.CONTACTS -> ContactsScreen(viewModel = viewModel)
          AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
          AppScreen.RESPONDERS -> ResponderNetworkScreen(viewModel = viewModel)
        }
      }
    }
  }

  if (showOnboarding) {
    OnboardingDialog(
      viewModel = viewModel,
      onDismiss = { viewModel.closeOnboarding() }
    )
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}

