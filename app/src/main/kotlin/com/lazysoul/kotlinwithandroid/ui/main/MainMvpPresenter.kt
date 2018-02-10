package com.lazysoul.kotlinwithandroid.ui.main

import com.lazysoul.kotlinwithandroid.common.BaseMvpPresenter
import com.lazysoul.kotlinwithandroid.common.BaseMvpView
import com.lazysoul.kotlinwithandroid.common.RxPresenter
import com.lazysoul.kotlinwithandroid.singletons.TodoManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created by Lazysoul on 2017. 7. 9..
 */

class MainMvpPresenter<MvpView : BaseMvpView> : RxPresenter(), BaseMvpPresenter<MvpView> {

    private var view: MainMvpView? = null

    private val searchTextChangeSubject = PublishSubject.create<String>()

    init {
        add(searchTextChangeSubject
                .throttleLast(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text ->
                    val searchedList = TodoManager.search(text)
                    if (searchedList.isEmpty()) {
                        view!!.showEmptyView()
                    } else {
                        view!!.onUpdateTodoList(searchedList)
                    }
                })
    }

    override fun attachView(view: MvpView) {
        this.view = view as MainMvpView
    }

    override fun destroy() {
        dispose()
    }

     fun createTodoSamples() {
        view!!.onCreatedSamples(TodoManager.createSamples())
    }

     fun loadTodoList() {
        val todoList = TodoManager.getTodoList()
        if (null != todoList && todoList.isEmpty()) {
            view!!.showEmptyView()
        } else {
            view!!.onUpdateTodoList(todoList)
        }
    }

     fun insert(id: Int, body: String) {
        view!!.onCreatedTodo(TodoManager.insert(id, body))
    }

     fun update(id: Int, body: String) {
        val todo = TodoManager.update(id, body)
        if (todo != null) {
            view!!.onUpdateTodo(todo)
        }
    }

     fun checked(id: Int, isChecked: Boolean) {
        TodoManager.checked(id, isChecked)
    }

     fun searchQuery(text: String) {
        searchTextChangeSubject.onNext(text)
    }

     fun searchFinish() {
        loadTodoList()
    }
}
