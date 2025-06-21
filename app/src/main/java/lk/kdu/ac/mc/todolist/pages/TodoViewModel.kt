//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp

package lk.kdu.ac.mc.todolist.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class TodoViewModel : ViewModel() {

    private var _todoList = MutableLiveData<List<Todo>>()
    val todoList : LiveData<List<Todo>> = _todoList

    init {
        getAllTodo()
    }


    fun getAllTodo(){
        _todoList.value = TodoManager.getAllTodo().reversed()
    }

    fun addTodo(title: String, topic: String, category: String, taskDate: Date?) {
        TodoManager.addTodo(title, topic, category, taskDate)
        getAllTodo()
    }

    fun deleteTodo(id : Int){
        TodoManager.deleteTodo(id)
        getAllTodo()
    }


}