//knowlage :https://www.youtube.com/watch?v=sWOmlDvz_3U&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=8 , chatGpt Debugging

package lk.kdu.ac.mc.todolist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lk.kdu.ac.mc.todolist.pages.Todo

@Database(entities = [Todo::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase(){

    companion object {
        const val NAME = "Todo_DB"
    }

    abstract fun getTodoDao() : TodoDao

}