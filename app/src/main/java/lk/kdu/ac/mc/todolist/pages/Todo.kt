//knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7
//source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp

package lk.kdu.ac.mc.todolist.pages

import java.time.Instant
import java.util.Date

data class Todo(
    var id: Int,
    var title: String,
    var topic: String,
    var category: String = "No Category",
    var createdAt: Date,
    var taskDate: Date? = null
)




