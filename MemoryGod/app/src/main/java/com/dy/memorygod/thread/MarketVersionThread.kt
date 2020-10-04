package com.dy.memorygod.thread

import com.dy.memorygod.manager.PatternManager
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MarketVersionThread(private val packageName: String) : Thread() {

    private var version: String? = null

    override fun run() {
        super.run()
        this.version = getMarketVersion()
    }

    private fun getMarketVersion(): String? {
        return try {
            val url = "https://play.google.com/store/apps/details?id=$packageName"
            val doc: Document = Jsoup.connect(url).get()
            val version: Elements = doc.select(".htlgb")

            for (i in 0..19) {
                val text: String = version[i].text()
                if (PatternManager.isMarketVersion(text)) {
                    return text
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