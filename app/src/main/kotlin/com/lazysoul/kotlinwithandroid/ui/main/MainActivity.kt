package com.lazysoul.kotlinwithandroid.ui.main

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.lazysoul.kotlinwithandroid.R
import com.lazysoul.kotlinwithandroid.common.BaseActivity
import com.lazysoul.kotlinwithandroid.datas.Todo
import com.lazysoul.kotlinwithandroid.singletons.TodoManager
import com.lazysoul.kotlinwithandroid.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMvpView {

    private lateinit var todoAdapter: TodoAdapter

    private val REQUEST_CODE_DETAIL = 100

    @Inject lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var editor: SharedPreferences.Editor

    lateinit var presenter: MainMvpPresenter<MainMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(tb_activity_main)

        rv_activity_main.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(
                { id, checked -> presenter.checked(id, checked) },
                { goTodo(it) }
        )
        rv_activity_main.adapter = todoAdapter

        fa_activity_main.setOnClickListener { createTodo() }

        if (sharedPreferences.getBoolean(KEY_IS_FIRST, true)) {
            presenter.createTodoSamples()
            editor.putBoolean(KEY_IS_FIRST, false).apply()
        } else {
            presenter.loadTodoList()
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DETAIL) {
            val resultType = data.getIntExtra(TodoManager.KEY_RESULT_TYPE, -1)
            val id = data.getIntExtra(TodoManager.KEY_ID, -1)
            val body = data.getStringExtra(TodoManager.KEY_BODY)
            when (resultType) {
                TodoManager.RESULT_TYPE_CREATED -> presenter.insert(id, body)
                TodoManager.RESULT_TYPE_UPDATED -> presenter.update(id, body)
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        setSearchView(menu.findItem(R.id.menu_activity_main_search))
        return super.onCreateOptionsMenu(menu)
    }

    override fun inject() {
        component.inject(this)
    }

    override fun initPresenter() {
        presenter = MainMvpPresenter()
        presenter.attachView(this)
    }

    override fun onUpdateTodoList(todoList: List<Todo>) {
        for ((id, body, isChecked) in todoList) {
            Log.d("foo", "id : $id, body : $body, checked : $isChecked")
        }
        todoAdapter.addItems(todoList)
        tv_activity_main_empty.visibility = View.GONE
    }

    override fun onUpdateTodo(todo: Todo) {
        todoAdapter.update(todo)
    }

    override fun onCreatedTodo(todo: Todo) {
        todoAdapter.addItem(todo)
    }

    override fun showEmptyView() {
        todoAdapter.clear()
        tv_activity_main_empty.visibility = View.VISIBLE
    }

    override fun onCreatedSamples(todoList: List<Todo>) {
        todoAdapter.addItems(todoList)
    }

    private fun createTodo() {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(TodoManager.KEY_REQUEST_TYPE, TodoManager.REQUEST_TYPE_CREATE)
        startActivityForResult(intent, REQUEST_CODE_DETAIL)
    }

    private fun goTodo(id: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(TodoManager.KEY_REQUEST_TYPE, TodoManager.REQUEST_TYPE_VIEW)
        intent.putExtra(TodoManager.KEY_ID, id)
        startActivityForResult(intent, REQUEST_CODE_DETAIL)
    }

    private fun setSearchView(searchMenuItem: MenuItem) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.hint_search)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(true)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.searchQuery(newText)
                return true
            }
        })

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                presenter.searchFinish()
                return true
            }
        })
    }

    companion object {

        private val KEY_IS_FIRST = "isFirst"
    }
}
