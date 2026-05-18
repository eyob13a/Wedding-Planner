package com.org.debrebirhan.weddingplanner.ui.screens.profile

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(viewModel: WeddingViewModel) {
    val primaryRose = Color(0xFFCD766D)
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // የጽሑፍ ሳጥኖቹ ስቴት (Edit Form States)
    var groomInput by remember { mutableStateOf("") }
    var brideInput by remember { mutableStateOf("") }
    var budgetInput by remember { mutableStateOf("") }

    // ገጹ በተከፈተ ቁጥር ቪውሞዴል ውስጥ ያለውን ወቅታዊ መረጃ በሳጥኖቹ ውስጥ ይጭናል
    LaunchedEffect(viewModel.groomName.value, viewModel.brideName.value, viewModel.totalBudget.value) {
        groomInput = viewModel.groomName.value
        brideInput = viewModel.brideName.value
        budgetInput = if (viewModel.totalBudget.value == 0.0) "" else viewModel.totalBudget.value.toString()
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // 📅 ተጠቃሚው የመረጠውን እውነተኛ ቀን ማሳያ ፎርማት ማድረጊያ
    val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val userSelectedDate = if (viewModel.weddingTimestamp.value > 0) {
        sdf.format(Date(viewModel.weddingTimestamp.value))
    } else {
        "May 21, 2026 (Default)"
    }

    // DatePicker Dialog - ተጠቃሚው ቀኑን ለመቀየር ሲጫነው የሚከፈት
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCal = Calendar.getInstance()
            selectedCal.set(year, month, dayOfMonth)
            viewModel.weddingTimestamp.value = selectedCal.timeInMillis
            Toast.makeText(context, "Wedding date updated successfully!", Toast.LENGTH_SHORT).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F5))
            .padding(20.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Manage Wedding Profile ⚙️",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🟢 1. READ DISPLAY BANNER (ተጠቃሚው የሞላውን መረጃ ማሳያ)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = primaryRose),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    // 🎯 እዚህ ጋ & የነበረው በትክክል በ ❤️ ምልክት ተተክቷል!
                    Text(
                        text = "${viewModel.groomName.value.ifBlank { "eyob" }} ❤️ ${viewModel.brideName.value.ifBlank { "someone" }}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Allocated Budget: ${if (viewModel.totalBudget.value == 0.0) 100.0 else viewModel.totalBudget.value} ETB",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Wedding Date: $userSelectedDate",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔵 2. UPDATE FORM (መረጃ ማስተካከያ)
        Text(text = "Update Wedding Info", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Groom Input
                OutlinedTextField(
                    value = groomInput,
                    onValueChange = { groomInput = it },
                    label = { Text("Groom's Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryRose) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bride Input
                OutlinedTextField(
                    value = brideInput,
                    onValueChange = { brideInput = it },
                    label = { Text("Bride's Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryRose) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Budget Input
                OutlinedTextField(
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    label = { Text("Total Allocated Budget (ETB)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryRose)
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Change Date", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.groomName.value = groomInput
                            viewModel.brideName.value = brideInput
                            viewModel.totalBudget.value = budgetInput.toDoubleOrNull() ?: 0.0
                            Toast.makeText(context, "Profile details updated!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Changes", fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔴 3. DANGER ZONE (ዳታ ማጥፊያ)
        Text(text = "Danger Zone", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFD32F2F))
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFFFCDD2))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Resetting the workspace will erase all onboarded couple configurations, custom tasks, budget lists, and invited guest logs from storage.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset All Wedding Data")
                }
            }
        }
    }

    // 🔴 የዲሊት ማረጋገጫ Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Reset Application State?") },
            text = { Text("Are you sure you want to permanently clear all registration fields, expenses entries, and guest files? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllData()
                        groomInput = ""
                        brideInput = ""
                        budgetInput = ""
                        showDeleteDialog = false
                        Toast.makeText(context, "All internal records cleared successfully.", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Text("Reset", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}