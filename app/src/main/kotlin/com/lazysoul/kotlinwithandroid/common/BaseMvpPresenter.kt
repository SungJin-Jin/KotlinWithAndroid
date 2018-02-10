package com.lazysoul.kotlinwithandroid.common

/**
 * Created by Lazysoul on 2017. 7. 9..
 */

interface BaseMvpPresenter<T : BaseMvpView> {

    fun attachView(view: T)

    fun destroy()
}
