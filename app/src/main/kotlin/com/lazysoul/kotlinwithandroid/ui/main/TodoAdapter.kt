package com.lazysoul.kotlinwithandroid.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lazysoul.kotlinwithandroid.R
import com.lazysoul.kotlinwithandroid.datas.Todo
import kotlinx.android.synthetic.main.item_todo.view.*
import java.util.*

/**
 * Created by Lazysoul on 2017. 7. 12..
 */

internal class TodoAdapter(private val todoListener: TodoListener) : RecyclerView.Adapter<TodoAdapter.TodoHolder>() {

    private val todoList = ArrayList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(parent)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        holder.draw(todoList[position])
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun addItems(list: List<Todo>) {
        todoList.clear()
        todoList.addAll(list)
        notifyDataSetChanged()
    }

    fun update(todo: Todo) {
        var position = -1
        for (i in todoList.indices) {
            if (todo.id == todoList[i].id) {
                todoList[i].body = todo.body
                position = i
                break
            }
        }
        notifyItemChanged(position)
    }

    fun addItem(todo: Todo) {
        todoList.add(todo)
        notifyItemInserted(todoList.size - 1)
    }

    fun clear() {
        todoList.clear()
        notifyDataSetChanged()
    }

    internal inner class TodoHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)) {
        fun draw(todo: Todo) {
            itemView.cb_item.isChecked = todo.isChecked
            itemView.cb_item.setOnClickListener {
                todo.isChecked = !todo.isChecked
                todoListener.onChecked(todo.id, todo.isChecked)
            }
            itemView.cv_item_todo.setOnClickListener { todoListener.onClicked(todo.id) }
            itemView.tv_item_todo_body.text = todo.body
        }
    }
}
