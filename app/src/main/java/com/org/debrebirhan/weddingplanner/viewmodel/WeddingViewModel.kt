package com.org.debrebirhan.weddingplanner.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

data class WeddingTask(val id: Int, val title: String, val isCompleted: Boolean = false)

data class WeddingExpense(val id: Int, val title: String, val amount: Double)

data class WeddingGuest(val id: Int, val name: String, val isInvited: Boolean = false)

data class InvitationTemplate(val id: Int, val themeName: String, val color: Color)

class WeddingViewModel : ViewModel() {
    // Core onboarding state variables
    var groomName = mutableStateOf("")
    var brideName = mutableStateOf("")
    var totalBudget = mutableStateOf(0.0)
    var weddingTimestamp = mutableStateOf(0L)

    // Dynamic list holding tasks added by the user
    val tasksList = mutableStateListOf<WeddingTask>()
    private var taskIdCounter = 1

    // Dynamic list holding recorded expenses
    val expensesList = mutableStateListOf<WeddingExpense>()
    private var expenseIdCounter = 1

    // Dynamic list holding registered wedding guests
    val guestsList = mutableStateListOf<WeddingGuest>()
    private var guestIdCounter = 1

    // Available invitation design portfolios
    val invitationTemplates = listOf(
        InvitationTemplate(1, "Classic Rose", Color(0xFFCD766D)),
        InvitationTemplate(2, "Royal Blue", Color(0xFF002D62)),
        InvitationTemplate(3, "Golden Grace", Color(0xFFD4AF37)),
        InvitationTemplate(4, "Modern Mint", Color(0xFF98FF98))
    )
    var selectedTemplate = mutableStateOf(invitationTemplates[0])

    // Adds a new custom wedding task
    fun addTask(title: String) {
        if (title.isNotBlank()) {
            tasksList.add(WeddingTask(id = taskIdCounter++, title = title))
        }
    }

    // Toggles task completion state from Dashboard
    fun toggleTaskCompletion(taskId: Int) {
        val index = tasksList.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val currentTask = tasksList[index]
            tasksList[index] = currentTask.copy(isCompleted = !currentTask.isCompleted)
        }
    }

    // Records a new spending expense
    fun addExpense(title: String, amount: Double) {
        if (title.isNotBlank() && amount > 0) {
            expensesList.add(WeddingExpense(id = expenseIdCounter++, title = title, amount = amount))
        }
    }

    // Dynamically calculates total spent amount from recorded expenses
    fun getSpentAmount(): Double {
        return expensesList.sumOf { it.amount }
    }

    // Appends a new guest entry to the data collection
    fun addGuest(name: String) {
        if (name.isNotBlank()) {
            guestsList.add(WeddingGuest(id = guestIdCounter++, name = name))
        }
    }

    // Flags the invitation dispatch state as confirmed for a specific guest
    fun toggleInvitation(guestId: Int) {
        val index = guestsList.indexOfFirst { it.id == guestId }
        if (index != -1) {
            val currentGuest = guestsList[index]
            guestsList[index] = currentGuest.copy(isInvited = true)
        }
    }
}