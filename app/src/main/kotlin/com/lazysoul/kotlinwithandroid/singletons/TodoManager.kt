package com.lazysoul.kotlinwithandroid.singletons

import com.lazysoul.kotlinwithandroid.datas.Todo

/**
 * Created by Lazysoul on 2017. 7. 12..
 */

object TodoManager {

    const val KEY_ID = "id"

    const val KEY_BODY = "body"

    const val KEY_REQUEST_TYPE = "request_type"

    const val KEY_RESULT_TYPE = "result_type"

    const val REQUEST_TYPE_CREATE = 100

    const val REQUEST_TYPE_VIEW = 101

    const val RESULT_TYPE_CREATED = 200

    const val RESULT_TYPE_UPDATED = 201

    private var todoList = mutableListOf<Todo>()

    val maxId: Int
        get() = todoList.maxBy { it.id }?.id ?: -1

    fun getTodoList(): List<Todo> {
        return todoList
    }

    fun createSamples(): List<Todo> {
        return (0..9).map { Todo(it, "Todo $it", false, false) }
                .toMutableList()
                .apply {
                    todoList = this
                }
    }

    fun getTodo(id: Int) = todoList.firstOrNull({ it.id == id })

    fun search(text: String) = when {
        text.isEmpty() -> todoList
        else -> todoList.filter { it.body.contains(text) }
    }

    fun insert(id: Int, body: String): Todo {
        val todo = Todo(id, body, false, false)
        todoList.add(todo)
        return todo
    }

    fun update(id: Int, body: String) =
            todoList.firstOrNull { it.id == id }
                    .apply { this?.body = body }

    fun checked(id: Int, isChecked: Boolean) {
        todoList.firstOrNull { it.id == id }?.isChecked = isChecked
    }
}
