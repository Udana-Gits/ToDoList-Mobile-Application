//knowlage :https://www.youtube.com/watch?v=sWOmlDvz_3U&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=8 , chatGpt Debugging

package lk.kdu.ac.mc.todolist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lk.kdu.ac.mc.todolist.pages.Todo

@Database(entities = [Todo::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class) // apply converters to convert date
abstract class TodoDatabase : RoomDatabase(){ // create roomdatabse named as tododatabase

    companion object {
        const val NAME = "Todo_DB"
    }

    abstract fun getTodoDao() : TodoDao // define tododao otherwise todoviewmodel cannot use it

}