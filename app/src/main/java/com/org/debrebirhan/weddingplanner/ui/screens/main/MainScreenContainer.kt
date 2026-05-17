package com.org.debrebirhan.weddingplanner.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.org.debrebirhan.weddingplanner.ui.screens.budget.BudgetScreen
import com.org.debrebirhan.weddingplanner.ui.screens.dashboard.DashboardScreen
import com.org.debrebirhan.weddingplanner.ui.screens.guests.GuestsScreen
import com.org.debrebirhan.weddingplanner.ui.screens.tasks.TasksScreen
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContainer(viewModel: WeddingViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val primaryRose = Color(0xFFCD766D)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryRose, indicatorColor = Color(0xFFFBECE4))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Tasks") },
                    label = { Text("Tasks") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryRose, indicatorColor = Color(0xFFFBECE4))
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Guests") },
                    label = { Text("Guests") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryRose, indicatorColor = Color(0xFFFBECE4))
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Budget") },
                    label = { Text("Budget") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryRose, indicatorColor = Color(0xFFFBECE4))
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryRose, indicatorColor = Color(0xFFFBECE4))
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> DashboardScreen(viewModel = viewModel)
                1 -> TasksScreen(viewModel = viewModel)
                2 -> GuestsScreen(viewModel = viewModel)
                3 -> BudgetScreen(viewModel = viewModel)
                else -> DashboardScreen(viewModel = viewModel)
            }
        }
    }
}