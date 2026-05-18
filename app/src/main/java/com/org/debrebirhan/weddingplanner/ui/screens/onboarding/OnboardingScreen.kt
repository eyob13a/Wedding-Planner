package com.org.debrebirhan.weddingplanner.ui.screens.onboarding

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel
import java.util.*

@Composable
fun OnboardingScreen(
    viewModel: WeddingViewModel, // 🎯 ቪውሞዴሉ እዚህ ጋር እንዲደርሰን ተጨምሯል
    onSetupComplete: (String, String, String, Long) -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }

    val backgroundColor = Color(0xFFFDF8F5)
    val primaryRose = Color(0xFFCD766D)
    val darkText = Color(0xFF4A3525)

    if (currentStep == 0) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                drawCircle(
                    color = Color(0xFFFBECE4),
                    radius = size.width * 0.75f,
                    center = Offset(size.width / 2, -size.height * 0.2f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 70.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(140.dp),
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(2.5.dp, primaryRose.copy(alpha = 0.7f)),
                        shadowElevation = 6.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🤵‍♂️", fontSize = 54.sp)
                                Text(text = "💐", fontSize = 32.sp, modifier = Modifier.offset(x = (-8).dp, y = 10.dp))
                                Text(text = "👰‍♀️", fontSize = 54.sp, modifier = Modifier.offset(x = (-14).dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Canvas(modifier = Modifier.size(width = 120.dp, height = 40.dp)) {
                        drawCircle(color = primaryRose, radius = 12f, center = Offset(size.width/2, size.height/2))
                        drawCircle(color = primaryRose.copy(alpha = 0.6f), radius = 10f, center = Offset(size.width/2 - 30, size.height/2))
                        drawCircle(color = primaryRose.copy(alpha = 0.6f), radius = 10f, center = Offset(size.width/2 + 30, size.height/2))
                        drawLine(color = Color(0xFFCD766D).copy(alpha = 0.2f), start = Offset(size.width/2, 0f), end = Offset(size.width/2, size.height))
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Wedding\nPlanner",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 38.sp,
                            lineHeight = 44.sp
                        ),
                        color = darkText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(
                        color = primaryRose.copy(alpha = 0.3f),
                        modifier = Modifier.width(120.dp),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Your perfect day, beautifully planned",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif
                        ),
                        color = darkText.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 60.dp)
                ) {
                    Button(
                        onClick = { currentStep = 1 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Get Started",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            color = Color.White
                        )
                    }
                }
            }
        }
    } else {
        var groomName by remember { mutableStateOf(viewModel.groomName.value) }
        var brideName by remember { mutableStateOf(viewModel.brideName.value) }
        var weddingBudget by remember { mutableStateOf(if(viewModel.totalBudget.value > 0) viewModel.totalBudget.value.toInt().toString() else "") }
        var selectedDateText by remember { mutableStateOf("Select Wedding Date") }
        var weddingTimestamp by remember { mutableStateOf(viewModel.weddingTimestamp.value) }

        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        // ከዚህ በፊት የተመረጠ ቀን ካለ እሱን በፅሁፍ ማሳያ
        if (weddingTimestamp != 0L) {
            val savedCalendar = Calendar.getInstance().apply { timeInMillis = weddingTimestamp }
            selectedDateText = "${savedCalendar.get(Calendar.DAY_OF_MONTH)}/${savedCalendar.get(Calendar.MONTH) + 1}/${savedCalendar.get(Calendar.YEAR)}"
        }

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val targetCalendar = Calendar.getInstance()
                targetCalendar.set(year, month, dayOfMonth, 0, 0, 0)
                weddingTimestamp = targetCalendar.timeInMillis
                selectedDateText = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Let's Get Started! ✨",
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold),
                    color = darkText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Enter your wedding details to initialize your plan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = darkText.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = groomName,
                    onValueChange = { groomName = it },
                    label = { Text("Groom's Name") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = brideName,
                    onValueChange = { brideName = it },
                    label = { Text("Bride's Name") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = weddingBudget,
                    onValueChange = { weddingBudget = it },
                    label = { Text("Total Budget (ETB)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = selectedDateText,
                    onValueChange = {},
                    label = { Text("Wedding Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Pick Date", tint = primaryRose)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryRose, focusedLabelColor = primaryRose),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).clickable { datePickerDialog.show() }
                )

                Button(
                    onClick = {
                        if (groomName.isNotBlank() && brideName.isNotBlank() && weddingBudget.isNotBlank() && weddingTimestamp != 0L) {
                            // 🎯 መጀመሪያ በቪውሞዴሉ ውስጥ ያሉትን ተለዋዋጮች እናድሳለን
                            viewModel.groomName.value = groomName
                            viewModel.brideName.value = brideName
                            viewModel.totalBudget.value = weddingBudget.toDoubleOrNull() ?: 0.0
                            viewModel.weddingTimestamp.value = weddingTimestamp

                            // 🎯 ወዲያውኑ ዳታው እንዳይጠፋ ስልኩ ማከማቻ (Preferences) ላይ እንቆልፈዋለን
                            viewModel.saveDataToStorage()

                            // ወደ ቀጣዩ ዳሽቦርድ ማለፍ
                            onSetupComplete(groomName, brideName, weddingBudget, weddingTimestamp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
                    shape = RoundedCornerShape(27.dp),
                    enabled = groomName.isNotBlank() && brideName.isNotBlank() && weddingBudget.isNotBlank() && weddingTimestamp != 0L
                ) {
                    Text(text = "Initialize Plan", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }
        }
    }
}