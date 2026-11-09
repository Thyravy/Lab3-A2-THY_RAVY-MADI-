package com.example.ravyy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ravyy.ui.theme.RavyyTheme

// Expense Data Class & Sample Data
data class Expense(
    val id: Int,
    val amount: Double,
    val currency: String,
    val category: String,
    val date: String,
    val remark: String?
)

val sampleExpenses = listOf(
    Expense(1, 45.50, "$", "Food", "2026-10-14", "Dinner with friends"),
    Expense(2, 120.00, "$", "Bills", "2026-10-12", "Electricity bill"),
    Expense(3, 15.00, "$", "Transport", "2026-10-10", null),
    Expense(4, 80.00, "$", "Shopping", "2026-10-05", "New shoes"),
    Expense(5, 25.00, "$", "Food", "2026-10-04", "Lunch"),
    Expense(6, 10.00, "$", "Transport", "2026-10-03", "Bus ticket"),
    Expense(7, 200.00, "$", "Bills", "2026-10-01", "Internet"),
    Expense(8, 50.00, "$", "Shopping", "2026-09-28", "T-shirt"),
    Expense(9, 30.00, "$", "Food", "2026-09-25", "Groceries")
)

class MainActivity : ComponentActivity() {
    private val TAG = "ravyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            RavyyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseDashboard(expenses = sampleExpenses)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

// Dashboard UI & Reusable Composables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDashboard(expenses: List<Expense>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ravyy") }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // D1: This month's total
            item {
                Spacer(modifier = Modifier.height(4.dp))
                TotalDisplayCard(expenses = expenses)
            }

            // D2: Spending by category
            item {
                CategorySpendingView(expenses = expenses)
            }

            // Section Header for D3
            item {
                Text(text = "Recent Expenses", style = MaterialTheme.typography.titleMedium)
            }

            // D3: Recent expenses list rows
            items(expenses) { expense ->
                ExpenseRow(expense = expense)
            }
        }
    }
}

@Composable
fun TotalDisplayCard(expenses: List<Expense>) {
    val totalAmount = expenses.sumOf { it.amount }
    val currency = expenses.firstOrNull()?.currency ?: "$"

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "This Month's Total", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$currency%.2f".format(totalAmount),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun CategorySpendingView(expenses: List<Expense>) {
    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Spending by Category", style = MaterialTheme.typography.titleMedium)
            for ((category, amount) in categoryTotals) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = category)
                    Text(text = "%.2f".format(amount))
                }
            }
        }
    }
}

// Reusable Composable for each row (D3)
@Composable
fun ExpenseRow(expense: Expense) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = expense.category, style = MaterialTheme.typography.bodyLarge)
                Text(text = expense.date, style = MaterialTheme.typography.bodySmall)
                if (!expense.remark.isNullOrBlank()) {
                    Text(text = expense.remark!!, style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                text = "${expense.currency}${expense.amount}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun ExpenseRowPreview() {
    RavyyTheme {
        ExpenseRow(
            expense = Expense(1, 45.50, "$", "Food", "2026-10-14", "Dinner")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    RavyyTheme {
        ExpenseDashboard(expenses = sampleExpenses)
    }
}