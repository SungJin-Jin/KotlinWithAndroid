package com.lazysoul.kotlinwithandroid.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lazysoul.kotlinwithandroid.KotlinWithAndroid
import com.lazysoul.kotlinwithandroid.injection.components.ActivityComponent
import com.lazysoul.kotlinwithandroid.injection.components.DaggerActivityComponent
import com.lazysoul.kotlinwithandroid.injection.module.ActivityModule

/**
 * Created by deskh on 2018-02-10.
 */

abstract class BaseActivity : AppCompatActivity(), BaseMvpView {

    protected lateinit var component: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = DaggerActivityComponent
                .builder()
                .applicationComponent(getApllicationComponent())
                .activityModule(ActivityModule(this))
                .build()

        inject()
        initPresenter()
    }

    fun getApllicationComponent() = (application as KotlinWithAndroid).component

}
