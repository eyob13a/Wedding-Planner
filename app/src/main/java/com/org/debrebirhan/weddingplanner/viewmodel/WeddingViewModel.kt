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

    // Available invitation design portfolios
    val invitationTemplates = listOf(
        InvitationTemplate(1, "Classic Rose", Color(0xFFCD766D)),
        InvitationTemplate(2, "Royal Blue", Color(0xFF002D62)),
        InvitationTemplate(3, "Golden Grace", Color(0xFFD4AF37)),
        InvitationTemplate(4, "Modern Mint", Color(0xFF98FF98))
    )
    var selectedTemplate = mutableStateOf(invitationTemplates[0])

    // 🎯 አፑ በጀመረ ቁጥር የተቀመጠውን መረጃ በራስ-ሰር ከስልኩ ላይ ይጭናል
    init {
        loadDataFromStorage()
    }

    // 💾 መረጃዎችን በቋሚነት ስልኩ ላይ ማስቀመጫ ፈንክሽን
    fun saveDataToStorage() {
        val editor = sharedPreferences.edit()
        editor.putString("groom_name", groomName.value)
        editor.putString("bride_name", brideName.value)
        editor.putLong("total_budget", totalBudget.value.toLong())
        editor.putLong("wedding_timestamp", weddingTimestamp.value)
        editor.putInt("selected_template_id", selectedTemplate.value.id)

        // ታስኮችን ወደ JSON ቀይሮ ማስቀመጥ
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

        // የወጪዎችን ሊስት ወደ JSON ቀይሮ ማስቀመጥ
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

        // የእንግዶችን ሊስት ወደ JSON ቀይሮ ማስቀመጥ
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


    // 📂 አፑ ሲከፈት የቆየውን ዳታ መልሶ ማምጫ ፈንክሽን
    private fun loadDataFromStorage() {
        groomName.value = sharedPreferences.getString("groom_name", "") ?: ""
        brideName.value = sharedPreferences.getString("bride_name", "") ?: ""
        totalBudget.value = sharedPreferences.getLong("total_budget", 0L).toDouble()
        weddingTimestamp.value = sharedPreferences.getLong("wedding_timestamp", 0L)

        val templateId = sharedPreferences.getInt("selected_template_id", 1)
        selectedTemplate.value = invitationTemplates.find { it.id == templateId } ?: invitationTemplates[0]

        // ታስኮችን መጫን
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

        // የወጪዎችን ሊስት መጫን
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

        // እንግዶችን መጫን
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

    // 🎯 አዲስ ታስክ ሲጨመር፡ 1ኛ. ወደ ታስክ ሊስት ይገባል፣ 2ኛ. ወዲያውኑ በባጀት ገፅ ላይ በ 0.0 ብር እንዲታይ ይደረጋል፣ 3ኛ. ስልኩ ማከማቻ ላይ ይቆለፋል!
    fun addTask(title: String) {
        if (title.isNotBlank()) {
            // 1. ታስኩን መመዝገብ
            tasksList.add(WeddingTask(id = taskIdCounter++, title = title, isCompleted = false))

            // 2. ወዲያውኑ በዚሁ የታስክ ስም በ 0.0 ብር አዲስ ወጪ በባጀት ሊስት ውስጥ መፍጠር
            val alreadyExists = expensesList.any { it.title.equals(title, ignoreCase = true) }
            if (!alreadyExists) {
                expensesList.add(WeddingExpense(id = expenseIdCounter++, title = title, amount = 0.0))
            }

            // 3. ዳታው እንዳይጠፋ ስልኩ ማከማቻ ላይ መቆለፍ
            saveDataToStorage()
        }
    }

    // Toggles task completion state from Dashboard
    fun toggleTaskCompletion(taskId: Int) {
        val index = tasksList.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val currentTask = tasksList[index]
            tasksList[index] = currentTask.copy(isCompleted = !currentTask.isCompleted)
            saveDataToStorage()
        }
    }


    // 🎯 አዲስ ወጪ መመዝገቢያ ወይም ከታስክ የመጣን ወጪ መሙያ ፈንክሽን (amount >= 0 መሆኑ ተስተካክሏል)
    fun addExpense(title: String, amount: Double) {
        if (title.isNotBlank() && amount >= 0) {
            // ከታስክ የመጣ ተመሳሳይ ስም ያለው ወጪ ካለ የብር መጠኑን ያድሰዋል እንጂ አዲስ አይፈጥርም
            val existingIndex = expensesList.indexOfFirst { it.title.equals(title, ignoreCase = true) }
            if (existingIndex != -1) {
                expensesList[existingIndex] = expensesList[existingIndex].copy(amount = amount)
            } else {
                expensesList.add(WeddingExpense(id = expenseIdCounter++, title = title, amount = amount))
            }
            saveDataToStorage()
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
            saveDataToStorage()
        }
    }

    // Flags the invitation dispatch state as confirmed for a specific guest
    fun toggleInvitation(guestId: Int) {
        val index = guestsList.indexOfFirst { it.id == guestId }
        if (index != -1) {
            val currentGuest = guestsList[index]
            guestsList[index] = currentGuest.copy(isInvited = !currentGuest.isInvited)
            saveDataToStorage()
        }
    }

    // 🔴 CRUD: Delete/Reset Operation (ሁሉንም መረጃዎች ሙሉ በሙሉ ከስልኩ ላይ ያጠፋል)
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
