package com.lazysoul.kotlinwithandroid.datas


data class Todo @JvmOverloads constructor (
        var id: Int,
        var body: String?,
        var isChecked: Boolean = false,
        var isFixed: Boolean = false
) {
    constructor() : this(-1, "", false, false)
}