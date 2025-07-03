//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp

package lk.kdu.ac.mc.todolist.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import lk.kdu.ac.mc.todolist.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLists(viewModel: TodoViewModel) {

    val context = LocalContext.current

    //to update existing tasks
    var editingTodo by remember { mutableStateOf<Todo?>(null) }


    val todoList by viewModel.todoList.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(editingTodo?.title ?: "") }
    var topicText by remember { mutableStateOf(editingTodo?.topic ?: "") }

    var selectedBottomSheetCategory by remember { mutableStateOf(editingTodo?.category ?: "No Category") }

    var searchText by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()

    val defaultCategories = listOf("All", "No Category", "Work", "Wishlist", "Birthday")

    var customCategories by remember { mutableStateOf(listOf<String>()) }


    //So even though customCategories is empty after restart, todoCategories rebuilds all used categories based on the saved Todos.
    val allCategories = remember(todoList, customCategories) {
        val todoCategories = todoList?.map { it.category }?.distinct().orEmpty()
        val combined = (defaultCategories + customCategories + todoCategories).distinct()// <-- no duplication
        derivedStateOf { combined }
    }

    var selectedCategory by remember { mutableStateOf("All") }




    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            //search box
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
            // search text accoding to the inputting string & Sort(sort with task dates)
            val filteredList = todoList?.filter {
                (selectedCategory == "All" || it.category == selectedCategory) &&
                        (it.title.contains(searchText, ignoreCase = true) || it.topic.contains(searchText, ignoreCase = true))
            }?.sortedWith(compareBy(
                { it.taskDate?.after(Date()) != true },
                { it.taskDate ?: Date(Long.MAX_VALUE) }
            )) ?: emptyList()
            //show image of the filterd list is empty
            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.emptygirl),
                        contentDescription = "Empty tasks Image",
                        modifier = Modifier
                            .size(350.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(content = {
                    itemsIndexed(filteredList) { _: Int, item ->
                        TodoItem(item = item,
                            onDelete = { viewModel.deleteTodo(item.id) },
                            onEdit = { editingTodo = item; showSheet = true }
                        )
                    }
                })
            }
        }

        FloatingActionButton(
            onClick = { showSheet = true },
            containerColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Todo", tint = MaterialTheme.colorScheme.primary)
        }

        LaunchedEffect(showSheet) {
            if (showSheet) {
                inputText = editingTodo?.title ?: ""
                topicText = editingTodo?.topic ?: ""
                selectedBottomSheetCategory = editingTodo?.category ?: "No Category"
            }
        }


        //bottom drewer pop from bottom used to add and edit tasks
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                val categoryOptions = defaultCategories.drop(1) + customCategories + "+ Create New"
                var expanded by remember { mutableStateOf(false) }

                val datePickerState = rememberDatePickerState(
                      initialSelectedDateMillis = editingTodo?.taskDate?.time
                )
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

                    //button to add and update (button update to avoid null inputs )
                    Button(
                        onClick = {
                            val taskDate = datePickerState.selectedDateMillis?.let { Date(it) }

                            if (inputText.isBlank() || topicText.isBlank() || selectedBottomSheetCategory.isBlank() || taskDate == null) {
                                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (editingTodo != null) {
                                viewModel.updateTodo(
                                    editingTodo!!.copy(
                                        title = inputText,
                                        topic = topicText,
                                        category = selectedBottomSheetCategory,
                                        taskDate = taskDate
                                    )
                                )
                            } else {
                                viewModel.addTodo(
                                    inputText,
                                    topicText,
                                    selectedBottomSheetCategory,
                                    taskDate
                                )
                            }

                            // Reset and close sheet
                            inputText = ""
                            topicText = ""
                            selectedBottomSheetCategory = "No Category"
                            editingTodo = null
                            coroutineScope.launch {
                                sheetState.hide()
                                showSheet = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(if (editingTodo != null) "Update" else "Add")
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
fun TodoItem(item: Todo, onDelete: () -> Unit, onEdit: () -> Unit) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "" + SimpleDateFormat("HH:mm:ss, dd/MM", Locale.ENGLISH).format(item.createdAt),
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_edit_square_24),
                            contentDescription = "Edit",
                            tint = Color.White
                        )
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


    }
}
