//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp

package lk.kdu.ac.mc.todolist.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import lk.kdu.ac.mc.todolist.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLists(viewModel: TodoViewModel) {
    val todoList by viewModel.todoList.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()

    val defaultCategories = listOf("All", "No Category", "Work", "Wishlist", "Birthday")
    var customCategories by remember { mutableStateOf(listOf<String>()) }
    val allCategories = remember { derivedStateOf { defaultCategories + customCategories } }
    var selectedCategory by remember { mutableStateOf("All") }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search tasks") },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Category Filter Bar
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allCategories.value.size) { index ->
                    val category = allCategories.value[index]
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            // Filter & Sort
            val filteredList = todoList?.filter {
                (selectedCategory == "All" || it.category == selectedCategory) &&
                        (it.title.contains(searchText, ignoreCase = true) || it.topic.contains(searchText, ignoreCase = true))
            }?.sortedWith(compareBy(
                { it.taskDate?.after(Date()) != true },
                { it.taskDate ?: Date(Long.MAX_VALUE) }
            )) ?: emptyList()

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.emptytasks),
                        contentDescription = "Empty tasks Image",
                        modifier = Modifier
                            .size(250.dp)
                            .align(Alignment.Center) // Now this works correctly
                    )
                }
            } else {
                LazyColumn(content = {
                    itemsIndexed(filteredList) { _: Int, item ->
                        TodoItem(item = item, onDelete = {
                            viewModel.deleteTodo(item.id)
                        })
                    }
                })
            }
        }

        FloatingActionButton(
            onClick = { showSheet = true },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                .border(
                width = 2.dp,
                color = Color.White,
                    shape = RoundedCornerShape(12.dp)
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Todo", tint = Color.White)
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                var topicText by remember { mutableStateOf("") }
                val categoryOptions = defaultCategories.drop(1) + customCategories + "+ Create New"
                var selectedBottomSheetCategory by remember { mutableStateOf("No Category") }
                var expanded by remember { mutableStateOf(false) }

                val datePickerState = rememberDatePickerState()
                var showDatePicker by remember { mutableStateOf(false) }
                var showNewCategoryDialog by remember { mutableStateOf(false) }
                var newCategoryName by remember { mutableStateOf("") }

                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    OutlinedTextField(
                        value = topicText,
                        onValueChange = { topicText = it },
                        label = { Text("Topic") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("Todo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = { expanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(selectedBottomSheetCategory)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                categoryOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            if (option == "+ Create New") {
                                                showNewCategoryDialog = true
                                            } else {
                                                selectedBottomSheetCategory = option
                                            }
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Select Task Date")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val taskDate = datePickerState.selectedDateMillis?.let { Date(it) }
                                viewModel.addTodo(
                                    inputText,
                                    topicText,
                                    selectedBottomSheetCategory,
                                    taskDate
                                )
                                inputText = ""
                                topicText = ""
                                selectedBottomSheetCategory = "No Category"
                                coroutineScope.launch {
                                    sheetState.hide()
                                    showSheet = false
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Add")
                    }
                }

                if (showNewCategoryDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNewCategoryDialog = false
                            newCategoryName = ""
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                if (newCategoryName.isNotBlank()) {
                                    customCategories = customCategories + newCategoryName
                                    selectedBottomSheetCategory = newCategoryName
                                }
                                newCategoryName = ""
                                showNewCategoryDialog = false
                            }) {
                                Text("Add")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showNewCategoryDialog = false
                                newCategoryName = ""
                            }) {
                                Text("Cancel")
                            }
                        },
                        title = { Text("New Category") },
                        text = {
                            OutlinedTextField(
                                value = newCategoryName,
                                onValueChange = { newCategoryName = it },
                                label = { Text("Category Name") }
                            )
                        }
                    )
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("OK")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(item: Todo, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "" + SimpleDateFormat("HH:mm:ss, dd/MM", Locale.ENGLISH).format(item.createdAt),
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Text(
                text = item.topic,
                fontSize = 25.sp,
                color = Color.White
            )
            Text(
                text = item.title,
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = item.category,
                fontSize = 14.sp,
                color = Color.Yellow
            )
            item.taskDate?.let {
                Text(
                    text = "Due Date: " + SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(it),
                    fontSize = 12.sp,
                    color = Color.Yellow
                )
            }
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}
