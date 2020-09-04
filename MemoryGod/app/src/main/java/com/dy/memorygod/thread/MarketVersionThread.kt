package com.dy.memorygod.thread

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.regex.Pattern

class MarketVersionThread(private val packageName: String) : Thread() {

    private var version: String? = null

    override fun run() {
        super.run()
        this.version = getMarketVersion()
    }

    private fun getMarketVersion(): String? {
        return try {
            val doc: Document =
                Jsoup.connect("https://play.google.com/store/apps/details?id=$packageName").get()
            val version: Elements = doc.select(".htlgb ")

            for (i in 0..19) {
                val marketVersion: String = version[i].text()
                if (Pattern.matches("^[0-9].[0-9].[0-9]$", marketVersion)) {
                    return marketVersion
                }
            }

            null
        } catch (ex: Exception) {
            null
        }
    }

    fun getVersion(): String? {
        return version
    }
}