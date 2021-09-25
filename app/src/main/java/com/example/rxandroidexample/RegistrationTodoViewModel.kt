package com.example.rxandroidexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rxandroidexample.room.Todo
import com.example.rxandroidexample.room.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationTodoViewModel(private val todoDb: TodoDatabase): ViewModel(){

    class Factory constructor(
        private val todoDb: TodoDatabase
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationTodoViewModel::class.java)) {
                return RegistrationTodoViewModel(todoDb) as T
            }
            throw IllegalArgumentException("Factory cannot make ViewModel of type ${modelClass.simpleName}")
        }
    }

    val title = MutableLiveData("")
    val description = MutableLiveData("")

    fun addTodo() {
        val title = title.value
        val description = description.value
        if (title != null && description != null) {
            CoroutineScope(Dispatchers.IO).launch {
                todoDb.todoDao().insert(Todo(title, description))
            }
        }
    }
}