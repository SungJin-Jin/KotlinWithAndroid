package com.lazysoul.kotlinwithandroid.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.lazysoul.kotlinwithandroid.R
import com.lazysoul.kotlinwithandroid.common.BaseActivity
import com.lazysoul.kotlinwithandroid.datas.Todo
import com.lazysoul.kotlinwithandroid.singletons.TodoManager
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by Lazysoul on 2017. 7. 15..
 */

class DetailActivity : BaseActivity(), DetailMvpView {

    lateinit var presenter: DetailMvpPresenter<DetailMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(tb_activity_detail)

        et_activity_detail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                presenter.onTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        presenter.loadTodo(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onBackPressed() {
        if (!presenter.isFixed()) {
            super.onBackPressed()
        } else {
            showSaveDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_detail, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val saveMenu = menu.findItem(R.id.menu_activity_main_save)
        saveMenu.isVisible = presenter.isFixed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_activity_main_save -> presenter.saveTodo(et_activity_detail.text.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSaveDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.msg_not_save)
                .setPositiveButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
                    super@DetailActivity.onBackPressed()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
    }

    override fun inject() {
        component.inject(this)
    }

    override fun initPresenter() {
        presenter = DetailMvpPresenter()
        presenter.attachView(this)
    }

    override fun onUpdated(todo: Todo, editable: Boolean) {
        et_activity_detail.setText(todo.body)
        if (editable) {
            et_activity_detail.requestFocus()
        }
    }

    override fun onChangedSaveBt() {
        invalidateOptionsMenu()
    }

    override fun onSaved(requestType: Int, todo: Todo) {
        var result = -1
        when (requestType) {
            TodoManager.REQUEST_TYPE_CREATE -> result = TodoManager.RESULT_TYPE_CREATED
            TodoManager.REQUEST_TYPE_VIEW -> result = TodoManager.RESULT_TYPE_UPDATED
        }

        val resultData = Intent()
        resultData.putExtra(TodoManager.KEY_RESULT_TYPE, result)
        resultData.putExtra(TodoManager.KEY_ID, todo.id)
        resultData.putExtra(TodoManager.KEY_BODY, todo.body)
        setResult(Activity.RESULT_OK, resultData)
    }
}
