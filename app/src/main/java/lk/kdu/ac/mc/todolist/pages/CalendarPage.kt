package lk.kdu.ac.mc.todolist.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CalendarPage() {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Calendar (Static Grid)", modifier = Modifier.padding(bottom = 16.dp))

        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            daysOfWeek.forEach {
                Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Simulate a 5-week calendar view (35 days)
        val totalDays = 35
        val dayNumbers = (1..30).toList() + List(5) { 0 } // pad remaining

        for (week in 0 until 5) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                for (day in 0 until 7) {
                    val dayIndex = week * 7 + day
                    val dayValue = dayNumbers.getOrElse(dayIndex) { 0 }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .background(if (dayValue != 0) Color.LightGray else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayValue != 0) {
                            Text(dayValue.toString())
                        }
                    }
                }
            }
        }
    }
}

