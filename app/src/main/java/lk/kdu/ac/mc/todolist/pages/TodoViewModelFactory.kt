//The crash you're seeing is because TodoViewModel requires a Context in its constructor, but you're trying to retrieve it like a regular ViewModel using:
//val todoViewModel: TodoViewModel = viewModel()
//This only works for parameterless ViewModels. Since yours now takes a parameter (Context), you must use a ViewModelProvider.Factory to create it properly

package lk.kdu.ac.mc.todolist.pages

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TodoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
