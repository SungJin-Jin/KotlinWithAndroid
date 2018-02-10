package com.lazysoul.kotlinwithandroid.singletons

import com.lazysoul.kotlinwithandroid.datas.Todo
import java.util.*

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

    private val todoList = ArrayList<Todo>()

    val maxId: Int
        get() {
            var max = -1
            for ((id) in todoList) {
                if (id > max) {
                    max = id
                }
            }
            return max
        }

    fun getTodoList(): List<Todo> {
        return todoList
    }

    fun createSamples(): ArrayList<Todo> {
        for (i in 0..9) {
            val todo = Todo(i, "Todo " + i, false, false)
            todoList.add(todo)
        }
        return todoList
    }

    fun getTodo(id: Int): Todo? {
        for (todo in todoList) {
            if (todo.id == id) {
                return todo
            }
        }
        return null
    }

    fun search(text: String): ArrayList<Todo> {
        val result = ArrayList<Todo>()
        if (text.isEmpty()) {
            result.addAll(todoList)
        } else {

            for (todo in todoList) {
                if (todo.body!!.contains(text)) {
                    result.add(todo)
                }
            }
        }
        return result
    }

    fun insert(id: Int, body: String): Todo {
        val todo = Todo(id, body, false, false)
        todoList.add(todo)
        return todo
    }

    fun update(id: Int, body: String): Todo? {
        for (todo in todoList) {
            if (todo.id == id) {
                todo.body = body
                return todo
            }
        }
        return null
    }

    fun checked(id: Int, isChecked: Boolean) {
        for (todo in todoList) {
            if (todo.id == id) {
                todo.isChecked = isChecked
                break
            }
        }
    }
}
