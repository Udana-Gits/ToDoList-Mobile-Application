//knowlage :https://www.youtube.com/watch?v=sWOmlDvz_3U&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=8 , chatGpt Debugging
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/4_TodoApp_RoomDB

package lk.kdu.ac.mc.todolist.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

class Converters {

    @TypeConverter
    fun fromDate(date : Date) : Long{
        return date.time
    }

    @TypeConverter
    fun toDate(time : Long) : Date{
        return Date(time)
    }

}