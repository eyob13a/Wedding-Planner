package com.org.debrebirhan.weddingplanner

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.org.debrebirhan.weddingplanner.ui.screens.main.MainScreenContainer
import com.org.debrebirhan.weddingplanner.ui.screens.onboarding.OnboardingScreen
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel

class MainActivity : ComponentActivity() {
    private val weddingViewModel: WeddingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Context persistence using SharedPreferences
        val sharedPreferences = getSharedPreferences("WeddingPlannerPrefs", Context.MODE_PRIVATE)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var isSetupComplete by remember {
                        mutableStateOf(sharedPreferences.getBoolean("isSetupComplete", false))
                    }

                    // Preload saved data into ViewModel state on launch
                    LaunchedEffect(Unit) {
                        weddingViewModel.groomName.value = sharedPreferences.getString("groomName", "") ?: ""
                        weddingViewModel.brideName.value = sharedPreferences.getString("brideName", "") ?: ""
                        weddingViewModel.totalBudget.value = sharedPreferences.getString("weddingBudget", "0")?.toDoubleOrNull() ?: 0.0
                        weddingViewModel.weddingTimestamp.value = sharedPreferences.getLong("weddingTimestamp", 0L)
                    }

                    if (!isSetupComplete) {
                        OnboardingScreen(onSetupComplete = { g, b, bud, time ->
                            weddingViewModel.groomName.value = g
                            weddingViewModel.brideName.value = b
                            weddingViewModel.totalBudget.value = bud.toDoubleOrNull() ?: 0.0
                            weddingViewModel.weddingTimestamp.value = time
                            isSetupComplete = true

                            // Commit data securely to local device storage
                            sharedPreferences.edit().apply {
                                putBoolean("isSetupComplete", true)
                                putString("groomName", g)
                                putString("brideName", b)
                                putString("weddingBudget", bud)
                                putLong("weddingTimestamp", time)
                                apply()
                            }
                        })
                    } else {
                        // Forward view context straight to main interface
                        MainScreenContainer(viewModel = weddingViewModel)
                    }
                }
            }
        }
    }
}