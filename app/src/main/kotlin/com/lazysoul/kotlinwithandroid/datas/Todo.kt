package com.lazysoul.kotlinwithandroid.datas


data class Todo (
        var id: Int = -1,
        var body: String = "",
        var isChecked: Boolean = false,
        var isFixed: Boolean = false
)