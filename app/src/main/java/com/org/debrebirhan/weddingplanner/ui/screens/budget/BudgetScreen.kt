package com.org.debrebirhan.weddingplanner.ui.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel

@Composable
fun BudgetScreen(viewModel: WeddingViewModel) {
    val totalBudget = viewModel.totalBudget.value
    val spentAmount = viewModel.getSpentAmount()
    val remainingBudget = totalBudget - spentAmount

    // የመሙያ ሣጥን ስቴቶች
    var expenseTitle by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

    val primaryRose = Color(0xFFCD766D)
    val darkText = Color(0xFF4A3525)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F5))
            .padding(20.dp)
    ) {
        Text(
            text = "Track Expenses 💰",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = darkText
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 1️⃣ 🎯 ትልልቅ የባጀት ማሳያ ካርዶች (Total & Remaining Budget)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Total Budget ካርድ
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBECE4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Budget", fontSize = 13.sp, color = darkText.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${totalBudget.toInt()} ETB", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = darkText)
                }
            }

            // Remaining Budget ካርድ (ባጀት ካለቀ በቀይ ቀለም ይተካል)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (remainingBudget >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Remaining", fontSize = 13.sp, color = darkText.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${remainingBudget.toInt()} ETB",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (remainingBudget >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2️⃣ 🎯 መረጃ መሙያ ሣጥኖች (Input Layout)
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
                // አዲስ ለመመዝገብ ወይም ከታስክ የመጣን ለማደስ ስም መኖሩ እና ብሩ ከ 0 በላይ መሆኑን ያረጋግጣል
                if (expenseTitle.isNotBlank() && amount >= 0) {
                    viewModel.addExpense(expenseTitle, amount)
                    expenseTitle = ""
                    expenseAmount = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Record Expense", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Expense History (Click item to quick fill)",
            style = MaterialTheme.typography.titleMedium,
            color = darkText,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        // 3️⃣ 🎯 የወጪዎች ዝርዝር (LazyColumn) - ክሊክ ሲደረግ ፎርሙን በራስ-ሰር ይሞላል
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.expensesList) { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable {
                            // 🎯 የወጪ መስመሩ ሲነካ መግለጫውን እና ብሩን ወደ ላይኛው ፎርም ይልከዋል!
                            expenseTitle = expense.title
                            expenseAmount = if (expense.amount > 0) expense.amount.toInt().toString() else ""
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = expense.title, style = MaterialTheme.typography.bodyLarge, color = darkText)
                            if (expense.amount == 0.0) {
                                Text(
                                    text = "Tap to set amount",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (expense.amount > 0) "-${expense.amount.toInt()} ETB" else "0 ETB",
                                color = if (expense.amount > 0) Color(0xFFC62828) else Color.Gray,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Quick Fill",
                                tint = primaryRose.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
