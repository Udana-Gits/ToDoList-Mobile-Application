//knowlage :https://www.youtube.com/watch?v=sWOmlDvz_3U&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=8 , chatGpt Debugging

package lk.kdu.ac.mc.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lk.kdu.ac.mc.todolist.pages.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo ORDER BY createdAt DESC")
    fun getAllTodo() : LiveData<List<Todo>>

    @Insert
    fun addTodo(todo : Todo)

    @Query("Delete FROM Todo where id = :id")
    fun deleteTodo(id : Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(todo: Todo)


}

