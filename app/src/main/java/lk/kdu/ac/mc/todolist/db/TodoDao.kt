//knowlage :https://www.youtube.com/watch?v=sWOmlDvz_3U&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=8 , chatGpt Debugging

//Define DAO (Data Access Object) these DAo are used in TodoViewModel

package lk.kdu.ac.mc.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lk.kdu.ac.mc.todolist.pages.Todo
import java.util.Date

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo WHERE userId = :userId")
    fun getAllTodo(userId: String): LiveData<List<Todo>>

    @Insert
    fun addTodo(todo : Todo)

    @Query("Delete FROM Todo where id = :id")
    fun deleteTodo(id : Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(todo: Todo)

    @Query("UPDATE Todo SET title = :title, topic = :topic, category = :category, taskDate = :taskDate WHERE id = :id")
    fun updateTodo(id: Int, title: String, topic: String, category: String, taskDate: Date?)

    @Query("SELECT * FROM Todo WHERE userId = :userId")
    suspend fun getAllTodoNow(userId: String): List<Todo>


    @Query("DELETE FROM Todo")
    suspend fun clearAllTodos()




}

