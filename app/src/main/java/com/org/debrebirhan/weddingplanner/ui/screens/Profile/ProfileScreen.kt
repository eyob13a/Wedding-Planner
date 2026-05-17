package com.org.debrebirhan.weddingplanner.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

@Composable
fun ProfileScreen(viewModel: WeddingViewModel) {
    val primaryRose = Color(0xFFCD766D)
    val scrollState = rememberScrollState()

    val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val formattedDate = if (viewModel.weddingTimestamp.value > 0) {
        sdf.format(Date(viewModel.weddingTimestamp.value))
    } else {
        "Not Scheduled Yet"
    }

    val daysRemaining = if (viewModel.weddingTimestamp.value > 0) {
        val diff = viewModel.weddingTimestamp.value - System.currentTimeMillis()
        max(0L, diff / (1000 * 60 * 60 * 24)).toString()
    } else {
        "0"
    }

    val totalTasks = viewModel.tasksList.size
    val completedTasks = viewModel.tasksList.count { it.isCompleted }
    val totalGuests = viewModel.guestsList.size
    val invitedGuests = viewModel.guestsList.count { it.isInvited }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F5))
            .padding(20.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Wedding Profile 👤",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Couples Premium Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = primaryRose),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${viewModel.groomName.value.ifBlank { "Groom Name" }} & ${viewModel.brideName.value.ifBlank { "Bride Name" }}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Wedding Date: $formattedDate",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Local Countdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Days Until Celebration", color = Color.Gray, fontSize = 14.sp)
                    Text(text = "$daysRemaining Days Left", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryRose)
                }
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(36.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Analytics Summary Dashboard
        Text(text = "Planning Analytics Summary 📊", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnalyticsMiniCard(
                title = "Budget Status",
                value = "${viewModel.getSpentAmount()} ETB",
                subText = "Total Expenses Registered",
                icon = Icons.Default.CheckCircle,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            AnalyticsMiniCard(
                title = "Tasks Progress",
                value = "$completedTasks / $totalTasks",
                subText = "Completed Activities",
                icon = Icons.Default.CheckCircle,
                tint = primaryRose,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AnalyticsMiniCard(
                title = "Guest Invitation Tracker",
                value = "$invitedGuests / $totalGuests Sent",
                subText = "Total Confirmed Dispatches",
                icon = Icons.Default.DateRange,
                tint = Color(0xFF2196F3),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. System Specifications Specs Footer
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEBE9).copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Application Context Info", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text(text = "Package: com.org.debrebirhan.weddingplanner", color = Color.Gray, fontSize = 12.sp)
                    Text(text = "Build Architecture: Local Offline-First Architecture", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AnalyticsMiniCard(
    title: String,
    value: String,
    subText: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = subText, color = Color.Gray, fontSize = 11.sp)
        }
    }
}