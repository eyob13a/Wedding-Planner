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

        // አፑ ለመጀመሪያ ጊዜ እየተከፈተ መሆኑን (Onboarding ማለፉን) ብቻ ለመለየት የምንጠቀምበት Preferences
        val sharedPreferences = getSharedPreferences("WeddingPlannerPrefs", Context.MODE_PRIVATE)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var isSetupComplete by remember {
                        mutableStateOf(sharedPreferences.getBoolean("isSetupComplete", false))
                    }

                    if (!isSetupComplete) {
                        // 🎯 እዚህ ጋ አዲሱን አጠራር (viewModel = weddingViewModel) በትክክል አስገብተነዋል
                        OnboardingScreen(
                            viewModel = weddingViewModel,
                            onSetupComplete = { g, b, bud, time ->
                                isSetupComplete = true

                                // Onboarding ማለፉን ብቻ እዚህ ጋር እንመዘግባለን (ሌላው ዳታ በ ViewModel ውስጥ ተቀምጧል)
                                sharedPreferences.edit().apply {
                                    putBoolean("isSetupComplete", true)
                                    apply()
                                }
                            }
                        )
                    } else {
                        // Forward view context straight to main interface
                        MainScreenContainer(viewModel = weddingViewModel)
                    }
                }
            }
        }
    }
}