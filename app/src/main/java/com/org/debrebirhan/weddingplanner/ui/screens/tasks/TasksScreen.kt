package com.org.debrebirhan.weddingplanner.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel

@Composable
fun TasksScreen(viewModel: WeddingViewModel) {
    var taskTitle by remember { mutableStateOf("") }
    val primaryRose = Color(0xFFCD766D)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFDF8F5)).padding(20.dp)) {
        Text(text = "Create Wedding Task 📋", style = MaterialTheme.typography.titleLarge, color = Color(0xFF4A3525))
        Spacer(modifier = Modifier.height(16.dp))

        // Input field for new dynamic task entities
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            label = { Text("Task Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.addTask(taskTitle)
                taskTitle = "" // Clear entry state on successful insert
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "All Registered Tasks", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(viewModel.tasksList) { task ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Text(text = task.title, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}