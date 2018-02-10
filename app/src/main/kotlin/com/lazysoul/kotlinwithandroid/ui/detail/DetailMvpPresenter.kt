package com.lazysoul.kotlinwithandroid.ui.detail


import android.content.Intent
import com.lazysoul.kotlinwithandroid.common.BaseMvpPresenter
import com.lazysoul.kotlinwithandroid.common.BaseMvpView
import com.lazysoul.kotlinwithandroid.common.RxPresenter
import com.lazysoul.kotlinwithandroid.datas.Todo
import com.lazysoul.kotlinwithandroid.singletons.TodoManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lazysoul on 2017. 7. 15..
 */

class DetailMvpPresenter<MvpView : BaseMvpView> : RxPresenter(), BaseMvpPresenter<MvpView> {

    private var view: DetailMvpView? = null

    private var beforeTodo: Todo? = null

    private val textChangeSubject = PublishSubject.create<Boolean>()

    private var requestType = -1

    init {
        add(textChangeSubject
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isChanged ->
                    beforeTodo!!.isFixed = isChanged
                    view!!.onChangedSaveBt()
                })
    }

    override fun attachView(view: MvpView) {
        this.view = view as DetailMvpView
    }

    override fun destroy() {
        dispose()
    }

    fun loadTodo(intent: Intent) {
        requestType = intent.getIntExtra(TodoManager.KEY_REQUEST_TYPE, TodoManager.REQUEST_TYPE_CREATE)

        when (requestType) {
            TodoManager.REQUEST_TYPE_CREATE -> {
                beforeTodo = Todo()
                beforeTodo!!.id = -1
                beforeTodo!!.body = ""
                view!!.onUpdated(beforeTodo!!, true)
            }
            TodoManager.REQUEST_TYPE_VIEW -> {
                val id = intent.getIntExtra(TodoManager.KEY_ID, -1)
                if (id != -1) {
                    beforeTodo = Todo()
                    beforeTodo!!.id = id
                    view!!.onUpdated(beforeTodo!!, false)
                }
            }
        }
    }

    fun onTextChanged(s: String) {
        if (!beforeTodo!!.isFixed && isChanged(s)) {
            beforeTodo!!.isFixed = true
        }

        textChangeSubject.onNext(isChanged(s))
    }

    fun saveTodo(text: String) {
        beforeTodo!!.body = text
        beforeTodo!!.isFixed = false
        if (beforeTodo!!.id == -1) {
            beforeTodo!!.id = TodoManager.maxId + 1
        }
        view!!.onSaved(requestType, beforeTodo!!)

        textChangeSubject.onNext(false)
    }

    private fun isChanged(s: String): Boolean {
        return beforeTodo!!.body != s
    }

    fun isFixed(): Boolean {
        return beforeTodo!!.isFixed
    }
}
