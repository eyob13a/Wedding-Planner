package com.org.debrebirhan.weddingplanner.ui.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel

@Composable
fun BudgetScreen(viewModel: WeddingViewModel) {
    var expenseTitle by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    val primaryRose = Color(0xFFCD766D)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFDF8F5)).padding(20.dp)) {
        Text(text = "Track Expenses 💰", style = MaterialTheme.typography.titleLarge, color = Color(0xFF4A3525))
        Spacer(modifier = Modifier.height(16.dp))

        // Input layout configurations for new expense entries
        OutlinedTextField(
            value = expenseTitle,
            onValueChange = { expenseTitle = it },
            label = { Text("Expense Description") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Amount (ETB)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                val amount = expenseAmount.toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    viewModel.addExpense(expenseTitle, amount)
                    expenseTitle = ""
                    expenseAmount = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Record Expense")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Expense History", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(viewModel.expensesList) { expense ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = expense.title, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "-${expense.amount.toInt()} ETB", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}