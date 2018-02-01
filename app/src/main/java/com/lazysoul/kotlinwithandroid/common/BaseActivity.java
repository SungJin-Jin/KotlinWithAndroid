package com.lazysoul.kotlinwithandroid.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lazysoul.kotlinwithandroid.KotlinWithAndroid;
import com.lazysoul.kotlinwithandroid.injection.components.ActivityComponent;
import com.lazysoul.kotlinwithandroid.injection.components.ApplicationComponent;
import com.lazysoul.kotlinwithandroid.injection.components.DaggerActivityComponent;
import com.lazysoul.kotlinwithandroid.injection.module.ActivityModule;

/**
 * Created by Lazysoul on 2017. 7. 15..
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseMvpView {

    protected ActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = DaggerActivityComponent
                .builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();

        inject();
        initPresenter();
    }

    public ApplicationComponent getApplicationComponent() {
        return ((KotlinWithAndroid) getApplication()).getComponent();
    }
}
