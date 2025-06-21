//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp

package lk.kdu.ac.mc.todolist.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.Instant
import java.util.Date

object TodoManager {

    private val todoList = mutableListOf<Todo>()

    private val _todoList = MutableLiveData<List<Todo>>(todoList)
    val todoListLiveData: LiveData<List<Todo>> = _todoList


    fun getAllTodo() : List<Todo>{
        return todoList
    }



    fun addTodo(title: String, topic: String, category: String, taskDate: Date?) {
        val newTodo = Todo(
            id = todoList.size + 1,
            title = title,
            topic = topic,
            category = category,
            createdAt = Date.from(Instant.now()),
            taskDate = taskDate
        )
        todoList.add(newTodo)

        // Force LiveData update
        _todoList.value = ArrayList(todoList)
    }


    fun deleteTodo(id : Int){
        todoList.removeIf{
            it.id==id
        }
    }

}