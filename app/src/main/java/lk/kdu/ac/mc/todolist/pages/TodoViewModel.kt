//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/4_TodoApp_RoomDB

package lk.kdu.ac.mc.todolist.pages

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.kdu.ac.mc.todolist.MainApplication
import java.time.Instant
import java.util.Date

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class TodoViewModel : ViewModel() {

    val todoDao = MainApplication.todoDatabase.getTodoDao()


    val todoList : LiveData<List<Todo>> = todoDao.getAllTodo()

    val userEmail = Firebase.auth.currentUser?.email
    val userId = userEmail?.replace(".", "_")  // Firebase paths can't use "."

    var isBackupEnabled = mutableStateOf(false)  // Add this at the top inside the ViewModel




    fun addTodo(title: String, topic: String, category: String, taskDate: Date?) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTodo = Todo(
                title = title,
                topic = topic,
                category = category,
                taskDate = taskDate,
                createdAt = Date.from(Instant.now())
            )
            todoDao.addTodo(newTodo)

            // Auto backup if toggle is enabled
            if (isBackupEnabled.value) {
                val todos = todoDao.getAllTodoNow()  // You need to define this in TodoDao
                backupTodosToFirebase(todos)
            }
        }
    }

    fun deleteTodo(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(id)
            if (isBackupEnabled.value) {
                val todos = todoDao.getAllTodoNow()
                backupTodosToFirebase(todos)
            }
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.updateTodo(todo.id, todo.title, todo.topic, todo.category, todo.taskDate)
            if (isBackupEnabled.value) {
                val todos = todoDao.getAllTodoNow()
                backupTodosToFirebase(todos)
            }
        }
    }



    fun backupTodosToFirebase(todos: List<Todo>) {
        val userId = Firebase.auth.currentUser?.email?.replace(".", "_") ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/todos")

        dbRef.setValue(todos)
    }

    fun restoreTodosFromFirebase() {
        val userId = Firebase.auth.currentUser?.email?.replace(".", "_") ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/todos")

        dbRef.get().addOnSuccessListener { snapshot ->
            val todos = snapshot.children.mapNotNull { it.getValue(Todo::class.java) }
            viewModelScope.launch {
                todos.forEach { insertTodoIfNotExists(it) }
            }
        }
    }

    fun insertTodoIfNotExists(todo: Todo) = viewModelScope.launch {
        todoDao.insertIfNotExists(todo)
    }






}