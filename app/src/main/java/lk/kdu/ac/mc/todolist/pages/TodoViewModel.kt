//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/4_TodoApp_RoomDB

package lk.kdu.ac.mc.todolist.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.kdu.ac.mc.todolist.MainApplication
import java.time.Instant
import java.util.Date

class TodoViewModel : ViewModel() {

    val todoDao = MainApplication.todoDatabase.getTodoDao()


    val todoList : LiveData<List<Todo>> = todoDao.getAllTodo()


    fun addTodo(title: String, topic: String, category: String, taskDate: Date?) {
        viewModelScope.launch(Dispatchers.IO) {//used beacuse if we use Ui thread app crashes so that we use alternative thread
            todoDao.addTodo(
                Todo(
                    title = title,
                    topic = topic,
                    category = category,
                    taskDate = taskDate, // now nullable
                    createdAt = Date.from(Instant.now())
                )
            )
        }
    }

    fun deleteTodo(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(id)
        }
    }


}