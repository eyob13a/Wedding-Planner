package com.org.debrebirhan.weddingplanner.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import org.json.JSONArray
import org.json.JSONObject

data class WeddingTask(val id: Int, val title: String, val isCompleted: Boolean = false)
data class WeddingExpense(val id: Int, val title: String, val amount: Double)
data class WeddingGuest(val id: Int, val name: String, val isInvited: Boolean = false)
data class InvitationTemplate(val id: Int, val themeName: String, val color: Color)

class WeddingViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("wedding_planner_prefs", Context.MODE_PRIVATE)

    // Core onboarding state variables
    var groomName = mutableStateOf("")
    var brideName = mutableStateOf("")
    var totalBudget = mutableStateOf(0.0)
    var weddingTimestamp = mutableStateOf(0L)

    // Dynamic lists
    val tasksList = mutableStateListOf<WeddingTask>()
    private var taskIdCounter = 1

    val expensesList = mutableStateListOf<WeddingExpense>()
    private var expenseIdCounter = 1

    val guestsList = mutableStateListOf<WeddingGuest>()
    private var guestIdCounter = 1


    val invitationTemplates = listOf(
        InvitationTemplate(1, "Classic Rose", Color(0xFFCD766D)),
        InvitationTemplate(2, "Royal Blue", Color(0xFF002D62)),
        InvitationTemplate(3, "Golden Grace", Color(0xFFD4AF37)),
        InvitationTemplate(4, "Modern Mint", Color(0xFF98FF98)),
        InvitationTemplate(5, "Burgundy", Color(0xFF800020)),     // አዲስ 1
        InvitationTemplate(6, "Emerald", Color(0xFF0F5257)),      // አዲስ 2
        InvitationTemplate(7, "Lavender", Color(0xFFE6E6FA))     // አዲስ 3
    )
    var selectedTemplate = mutableStateOf(invitationTemplates[0])

    init {
        loadDataFromStorage()
    }

    fun saveDataToStorage() {
        val editor = sharedPreferences.edit()
        editor.putString("groom_name", groomName.value)
        editor.putString("bride_name", brideName.value)
        editor.putLong("total_budget", totalBudget.value.toLong())
        editor.putLong("wedding_timestamp", weddingTimestamp.value)
        editor.putInt("selected_template_id", selectedTemplate.value.id)

        val tasksArray = JSONArray()
        tasksList.forEach { task ->
            val obj = JSONObject()
            obj.put("id", task.id)
            obj.put("title", task.title)
            obj.put("isCompleted", task.isCompleted)
            tasksArray.put(obj)
        }
        editor.putString("tasks_json", tasksArray.toString())
        editor.putInt("task_counter", taskIdCounter)

        val expensesArray = JSONArray()
        expensesList.forEach { expense ->
            val obj = JSONObject()
            obj.put("id", expense.id)
            obj.put("title", expense.title)
            obj.put("amount", expense.amount)
            expensesArray.put(obj)
        }
        editor.putString("expenses_json", expensesArray.toString())
        editor.putInt("expense_counter", expenseIdCounter)

        val guestsArray = JSONArray()
        guestsList.forEach { guest ->
            val obj = JSONObject()
            obj.put("id", guest.id)
            obj.put("name", guest.name)
            obj.put("isInvited", guest.isInvited)
            guestsArray.put(obj)
        }
        editor.putString("guests_json", guestsArray.toString())
        editor.putInt("guest_counter", guestIdCounter)

        editor.apply()
    }

    private fun loadDataFromStorage() {
        groomName.value = sharedPreferences.getString("groom_name", "") ?: ""
        brideName.value = sharedPreferences.getString("bride_name", "") ?: ""
        totalBudget.value = sharedPreferences.getLong("total_budget", 0L).toDouble()
        weddingTimestamp.value = sharedPreferences.getLong("wedding_timestamp", 0L)

        val templateId = sharedPreferences.getInt("selected_template_id", 1)
        selectedTemplate.value = invitationTemplates.find { it.id == templateId } ?: invitationTemplates[0]

        taskIdCounter = sharedPreferences.getInt("task_counter", 1)
        val tasksJson = sharedPreferences.getString("tasks_json", null)
        if (!tasksJson.isNullOrEmpty()) {
            tasksList.clear()
            val array = JSONArray(tasksJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                tasksList.add(WeddingTask(
                    id = obj.getInt("id"),
                    title = obj.getString("title"),
                    isCompleted = obj.getBoolean("isCompleted")
                ))
            }
        }

        expenseIdCounter = sharedPreferences.getInt("expense_counter", 1)
        val expensesJson = sharedPreferences.getString("expenses_json", null)
        if (!expensesJson.isNullOrEmpty()) {
            expensesList.clear()
            val array = JSONArray(expensesJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                expensesList.add(WeddingExpense(
                    id = obj.getInt("id"),
                    title = obj.getString("title"),
                    amount = obj.getDouble("amount")
                ))
            }
        }

        guestIdCounter = sharedPreferences.getInt("guest_counter", 1)
        val guestsJson = sharedPreferences.getString("guests_json", null)
        if (!guestsJson.isNullOrEmpty()) {
            guestsList.clear()
            val array = JSONArray(guestsJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                guestsList.add(WeddingGuest(
                    id = obj.getInt("id"),
                    name = obj.getString("name"),
                    isInvited = obj.getBoolean("isInvited")
                ))
            }
        }
    }

    fun addTask(title: String) {
        if (title.isNotBlank()) {
            tasksList.add(WeddingTask(id = taskIdCounter++, title = title, isCompleted = false))

            val alreadyExists = expensesList.any { it.title.equals(title, ignoreCase = true) }
            if (!alreadyExists) {
                expensesList.add(WeddingExpense(id = expenseIdCounter++, title = title, amount = 0.0))
            }

            saveDataToStorage()
        }
    }

    fun updateTask(taskId: Int, oldTitle: String, newTitle: String) {
        if (newTitle.isNotBlank()) {
            val taskIndex = tasksList.indexOfFirst { it.id == taskId }
            if (taskIndex != -1) {
                tasksList[taskIndex] = tasksList[taskIndex].copy(title = newTitle)
            }

            val expenseIndex = expensesList.indexOfFirst { it.title.equals(oldTitle, ignoreCase = true) }
            if (expenseIndex != -1) {
                expensesList[expenseIndex] = expensesList[expenseIndex].copy(title = newTitle)
            }

            saveDataToStorage()
        }
    }

    fun deleteTask(taskId: Int, taskTitle: String) {
        tasksList.removeAll { it.id == taskId }
        expensesList.removeAll { it.title.equals(taskTitle, ignoreCase = true) }
        saveDataToStorage()
    }

    fun toggleTaskCompletion(taskId: Int) {
        val index = tasksList.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val currentTask = tasksList[index]
            tasksList[index] = currentTask.copy(isCompleted = !currentTask.isCompleted)
            saveDataToStorage()
        }
    }

    fun addExpense(title: String, amount: Double) {
        if (title.isNotBlank() && amount >= 0) {
            val existingIndex = expensesList.indexOfFirst { it.title.equals(title, ignoreCase = true) }
            if (existingIndex != -1) {
                expensesList[existingIndex] = expensesList[existingIndex].copy(amount = amount)
            } else {
                expensesList.add(WeddingExpense(id = expenseIdCounter++, title = title, amount = amount))
            }
            saveDataToStorage()
        }
    }

    fun getSpentAmount(): Double {
        return expensesList.sumOf { it.amount }
    }

    fun addGuest(name: String) {
        if (name.isNotBlank()) {
            guestsList.add(WeddingGuest(id = guestIdCounter++, name = name))
            saveDataToStorage()
        }
    }

    fun toggleInvitation(guestId: Int) {
        val index = guestsList.indexOfFirst { it.id == guestId }
        if (index != -1) {
            val currentGuest = guestsList[index]
            guestsList[index] = currentGuest.copy(isInvited = !currentGuest.isInvited)
            saveDataToStorage()
        }
    }

    fun resetAllData() {
        groomName.value = ""
        brideName.value = ""
        totalBudget.value = 0.0
        weddingTimestamp.value = 0L
        selectedTemplate.value = invitationTemplates[0]

        tasksList.clear()
        expensesList.clear()
        guestsList.clear()

        taskIdCounter = 1
        expenseIdCounter = 1
        guestIdCounter = 1

        sharedPreferences.edit().clear().apply()
    }
}