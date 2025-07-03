package lk.kdu.ac.mc.todolist.pages
//show dates in a clender user have to do tasks in each month
//chat gpt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import lk.kdu.ac.mc.todolist.R
import java.time.DayOfWeek
import java.time.YearMonth
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(todoViewModel: TodoViewModel = viewModel()) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysOfWeek = DayOfWeek.values().map { it.name.take(3) }
    val todoList by todoViewModel.todoList.observeAsState(emptyList())
    val taskDates = remember(todoList) {
        todoList.mapNotNull {
            it.taskDate?.toInstant()?.atZone(java.time.ZoneId.systemDefault())?.toLocalDate()
        }.toSet()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
                        }
                        Text(
                            text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysOfWeek.forEach {
                            Text(
                                it,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val firstDayOfMonth = currentMonth.atDay(1)
                    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value % 7)
                    val totalDays = currentMonth.lengthOfMonth()
                    val paddedDays = List(firstDayOfWeek) { 0 } + (1..totalDays).toList()
                    val paddedDaysFixed = paddedDays + List((7 - paddedDays.size % 7) % 7) { 0 }

                    paddedDaysFixed.chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                val date = if (day != 0) currentMonth.atDay(day) else null
                                val hasTask = date in taskDates
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day != 0) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(day.toString(), fontSize = 14.sp)
                                            if (hasTask) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .background(Color.Red, shape = CircleShape)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.emptytasks),
                contentDescription = "Calendar Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
