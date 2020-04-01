package com.dy.memorygod.enums

import android.content.Context
import com.dy.memorygod.R

enum class ItemAddPosition(private val description: Int) {

    FIRST(R.string.app_item_add_position_first),
    LAST(R.string.app_item_add_position_last);

    companion object {

        fun getDescriptionArr(context: Context): Array<String> {
            val list = values().map { context.getString(it.description) }
            return list.toTypedArray()
        }

        fun get(idx: Int): ItemAddPosition? {
            return values().find { it.ordinal == idx }
        }

    }

}