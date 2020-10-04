package com.dy.memorygod.manager

import java.util.regex.Pattern

object PatternManager {

    fun isMarketVersion(str: String): Boolean {
        return (Pattern.matches("^[0-9]{1,2}.[0-9]{1,3}.[0-9]{1,3}$", str))
    }

}