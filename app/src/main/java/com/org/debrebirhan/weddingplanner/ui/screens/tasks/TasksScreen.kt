package com.org.debrebirhan.weddingplanner.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // 🎯 የጥላውን ስህተት ለማስተካከል የተጨመረ Import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel

@Composable
fun TasksScreen(viewModel: WeddingViewModel) {
    var taskTitle by remember { mutableStateOf("") }

    var editingTaskId by remember { mutableStateOf<Int?>(null) }
    var oldTaskTitle by remember { mutableStateOf("") }

    val primaryRose = Color(0xFFCD766D)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFDF8F5)).padding(20.dp)) {
        Text(
            text = if (editingTaskId == null) "Create Wedding Task 📋" else "Update Wedding Task ✏️",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF4A3525)
        )
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
                if (taskTitle.isNotBlank()) {
                    if (editingTaskId == null) {
                        viewModel.addTask(taskTitle)
                    } else {
                        viewModel.updateTask(editingTaskId!!, oldTaskTitle, taskTitle)
                        editingTaskId = null
                        oldTaskTitle = ""
                    }
                    taskTitle = "" // Clear entry state on successful insert
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (editingTaskId == null) "Add Task" else "Update Task")
        }

        if (editingTaskId != null) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = {
                    editingTaskId = null
                    taskTitle = ""
                    oldTaskTitle = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel Editing", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "All Registered Tasks", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))


        LazyColumn {
            items(viewModel.tasksList) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp)) // 🎯 ጥላው (Ripple Effect) ወደ ውጭ እንዳይፈስ እዚህ ጋር ቆረጥነው
                        .clickable {
                            editingTaskId = task.id
                            oldTaskTitle = task.title
                            taskTitle = task.title
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Task",
                                tint = primaryRose.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(20.dp)
                            )

                            IconButton(
                                onClick = {
                                    viewModel.deleteTask(task.id, task.title)

                                    if (editingTaskId == task.id) {
                                        editingTaskId = null
                                        taskTitle = ""
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Task",
                                    tint = Color(0xFFC62828)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
