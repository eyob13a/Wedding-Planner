package com.org.debrebirhan.weddingplanner.ui.screens.guests

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.org.debrebirhan.weddingplanner.ui.viewmodel.WeddingViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GuestsScreen(viewModel: WeddingViewModel) {
    var guestName by remember { mutableStateOf("") }
    val primaryRose = Color(0xFFCD766D)
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFDF8F5)).padding(20.dp)) {
        Text(text = "Guest Management 👥", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Total Guests Registered", color = Color.Gray)
                    Text(text = "${viewModel.guestsList.size} People", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryRose)
                }
                Icon(Icons.Default.Send, contentDescription = null, tint = primaryRose, modifier = Modifier.size(32.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = guestName,
                onValueChange = { guestName = it },
                label = { Text("Guest Full Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (guestName.isNotBlank()) {
                        viewModel.addGuest(guestName)
                        guestName = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryRose),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Choose Invitation Design", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

      
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(viewModel.invitationTemplates) { template ->
                val isSelected = viewModel.selectedTemplate.value == template

                Card(
                    modifier = Modifier
                        .size(110.dp, 65.dp)
                        .clickable { viewModel.selectedTemplate.value = template },
                    shape = RoundedCornerShape(12.dp), 
                    
                    border = BorderStroke(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.2f)
                    ),
                    colors = CardDefaults.cardColors(containerColor = template.color)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = template.themeName,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Guest List", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.guestsList) { guest ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(viewModel.selectedTemplate.value.color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = guest.name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = guest.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)

                        TextButton(
                            onClick = {
                                viewModel.toggleInvitation(guest.id)

                                val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
                                val weddingDate = sdf.format(Date(viewModel.weddingTimestamp.value))

                                val imageUri = generateWeddingCardImage(
                                    context = context,
                                    guestName = guest.name,
                                    groomName = viewModel.groomName.value.ifBlank { "Groom" },
                                    brideName = viewModel.brideName.value.ifBlank { "Bride" },
                                    dateText = weddingDate,
                                    backgroundColor = viewModel.selectedTemplate.value.color.toArgb()
                                )

                                if (imageUri != null) {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, imageUri)
                                        type = "image/png"
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Send Wedding Card Via"))
                                }
                            }
                        ) {
                            Text(
                                text = if (guest.isInvited) "Sent ✓" else "Send Card",
                                color = if (guest.isInvited) Color.Gray else primaryRose
                            )
                        }
                    }
                }
            }
        }
    }
}


fun generateWeddingCardImage(
    context: Context,
    guestName: String,
    groomName: String,
    brideName: String,
    dateText: String,
    backgroundColor: Int
): Uri? {
    val width = 800
    val height = 1200
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val goldColor = android.graphics.Color.parseColor("#D4AF37")

    // 1. Background Fill
    val bgPaint = Paint().apply { color = backgroundColor }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // 2. Framing Borders
    val borderPaint = Paint().apply {
        color = goldColor
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }
    canvas.drawRect(RectF(40f, 40f, (width - 40).toFloat(), (height - 40).toFloat()), borderPaint)

    borderPaint.strokeWidth = 2f
    canvas.drawRect(RectF(52f, 52f, (width - 52).toFloat(), (height - 52).toFloat()), borderPaint)

    // 3. Geometric Corner Ornaments
    val leafPaint = Paint().apply {
        color = goldColor
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun drawCornerOrnament(cx: Float, cy: Float, rotationDegrees: Float) {
        canvas.save()
        canvas.translate(cx, cy)
        canvas.rotate(rotationDegrees)

        val leafPath = AndroidPath()
        leafPath.moveTo(0f, 0f)
        leafPath.quadTo(30f, -10f, 60f, -60f)
        leafPath.quadTo(10f, -30f, 0f, 0f)
        canvas.drawPath(leafPath, leafPaint)

        leafPath.reset()
        leafPath.moveTo(0f, 0f)
        leafPath.quadTo(-10f, -30f, -60f, -60f)
        leafPath.quadTo(-30f, -10f, 0f, 0f)
        canvas.drawPath(leafPath, leafPaint)

        canvas.restore()
    }

    drawCornerOrnament(60f, 60f, 0f)
    drawCornerOrnament((width - 60).toFloat(), 60f, 90f)
    drawCornerOrnament((width - 60).toFloat(), (height - 60).toFloat(), 180f)
    drawCornerOrnament(60f, (height - 60).toFloat(), 270f)

    // 4. Typography and Drawing Components
    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)
    }

    textPaint.textSize = 36f
    textPaint.letterSpacing = 0.1f
    canvas.drawText("The Wedding Invitation", (width / 2).toFloat(), 150f, textPaint)

    val emojiPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 90f
    }

    canvas.drawText("🤵‍♂️", (width / 2 - 55).toFloat(), 280f, emojiPaint)
    canvas.drawText("💐", (width / 2 - 5).toFloat(), 300f, Paint().apply { isAntiAlias = true; textAlign = Paint.Align.CENTER; textSize = 50f })
    canvas.drawText("👰‍♀️", (width / 2 + 45).toFloat(), 280f, emojiPaint)

    // Divider Separator Line
    val dividerPaint = Paint().apply {
        color = goldColor
        strokeWidth = 3f
        isAntiAlias = true
    }
    canvas.drawCircle((width / 2).toFloat(), 350f, 6f, leafPaint)
    canvas.drawLine((width / 2 - 120).toFloat(), 350f, (width / 2 - 20).toFloat(), 350f, dividerPaint)
    canvas.drawLine((width / 2 + 20).toFloat(), 350f, (width / 2 + 120).toFloat(), 350f, dividerPaint)

    // Guest Welcome Wording Phrase Section
    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
    textPaint.textSize = 28f
    textPaint.color = android.graphics.Color.parseColor("#E0E0E0")
    canvas.drawText("With joyful hearts, we request the honor of", (width / 2).toFloat(), 440f, textPaint)
    canvas.drawText("the presence of", (width / 2).toFloat(), 485f, textPaint)

    // Dedicated Guest Specification Label
    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    textPaint.textSize = 54f
    textPaint.color = goldColor
    canvas.drawText(guestName, (width / 2).toFloat(), 580f, textPaint)

    // Intersecting Connecting Phrase Block
    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
    textPaint.textSize = 26f
    textPaint.color = android.graphics.Color.parseColor("#E0E0E0")
    canvas.drawText("at the holy matrimony celebrating the love of", (width / 2).toFloat(), 670f, textPaint)

    // Master Unified Couples Typography
    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    textPaint.textSize = 68f
    textPaint.color = android.graphics.Color.WHITE
    canvas.drawText(groomName, (width / 2).toFloat(), 780f, textPaint)

    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
    textPaint.textSize = 50f
    textPaint.color = android.graphics.Color.parseColor("#D32F2F")
    canvas.drawText("❤️", (width / 2).toFloat(), 865f, textPaint)

    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    textPaint.textSize = 68f
    textPaint.color = android.graphics.Color.WHITE
    canvas.drawText(brideName, (width / 2).toFloat(), 960f, textPaint)

    // Calendar Save-The-Date Block Footer Component
    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
    textPaint.textSize = 28f
    textPaint.color = android.graphics.Color.parseColor("#E0E0E0")
    canvas.drawText("Please save our date:", (width / 2).toFloat(), 1070f, textPaint)

    textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    textPaint.textSize = 36f
    textPaint.color = goldColor
    canvas.drawText(dateText, (width / 2).toFloat(), 1125f, textPaint)

    // 5. Compress safely into file cache storage
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/wedding_invitation.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val imageFile = File(cachePath, "wedding_invitation.png")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}