package com.org.debrebirhan.weddingplanner.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.max

@Composable
fun DashboardScreen(viewModel: WeddingViewModel) {
    val backgroundColor = Color(0xFFFDF8F5)
    val primaryRose = Color(0xFFCD766D)
    val darkText = Color(0xFF4A3525)

    // ⏱️ የቀናት ስሌት (Dynamic Countdown Calculation)
    val currentTime = System.currentTimeMillis()
    val timeDiff = viewModel.weddingTimestamp.value - currentTime
    val daysLeft = if (timeDiff > 0) TimeUnit.MILLISECONDS.toDays(timeDiff) else 0
    val daysLeftText = if (viewModel.weddingTimestamp.value == 0L) {
        "3 Days Left" // 👈 መጀመሪያ ቀን ካልተመረጠ ለዲፌንስ ማሳያነት 3 ቀን ይበል
    } else if (daysLeft > 0) {
        "$daysLeft Days Left"
    } else {
        "Today is the Big Day! 💍"
    }

    // 💰 የባጀት ስሌት (ከ Profile ገጽ ጋር በቀጥታ እንዲገናኝ የመነሻ ዋጋ 100 ተሰጥቶታል)
    val totalBudget = if (viewModel.totalBudget.value == 0.0) 100.0 else viewModel.totalBudget.value
    val spentAmount = viewModel.getSpentAmount()
    val remainingBudget = max(0.0, totalBudget - spentAmount)
    val progressFraction = if (totalBudget > 0) (spentAmount / totalBudget).toFloat() else 0.0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 🔗 🟢 የሙሽሮች ስም - እዚህ ጋ & የነበረው በ ❤️ ተተክቷል!
        Text(
            text = "${viewModel.groomName.value.ifBlank { "eyob" }} ❤️ ${viewModel.brideName.value.ifBlank { "someone" }}",
            style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = darkText
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Countdown Presentation Card
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(24.dp)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Days until your wedding", style = MaterialTheme.typography.bodyMedium, color = darkText.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = daysLeftText, style = MaterialTheme.typography.headlineLarge.copy(fontFamily = FontFamily.Serif, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = primaryRose))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Wedding checklist", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = darkText, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        // Conditional rendering for empty state illustration
        if (viewModel.tasksList.isEmpty()) {
            Text(
                text = "No tasks added yet. Go to 'Tasks' tab to initialize your list!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // Render user-configured tasks dynamically up to a preview threshold
            viewModel.tasksList.take(4).forEach { task ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { viewModel.toggleTaskCompletion(task.id) },
                    colors = CardDefaults.cardColors(containerColor = if (task.isCompleted) Color(0xFFF6F6F6) else Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (task.isCompleted) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = primaryRose)
                        } else {
                            Box(modifier = Modifier.size(24.dp).border(1.5.dp, darkText.copy(alpha = 0.3f), CircleShape))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = task.title, style = MaterialTheme.typography.bodyMedium, color = darkText)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Budget overview", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = darkText, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        // Financial Overview Presentation Block
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = primaryRose,
                    trackColor = Color(0xFFFBECE4),
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Spent: ${spentAmount.toInt()} ETB", style = MaterialTheme.typography.bodyMedium, color = darkText)
                    Text(text = "Remaining: ${remainingBudget.toInt()} ETB", style = MaterialTheme.typography.bodyMedium, color = primaryRose)
                }
            }
        }
    }
}