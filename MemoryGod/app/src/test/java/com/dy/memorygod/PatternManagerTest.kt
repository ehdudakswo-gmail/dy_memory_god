package com.dy.memorygod

import com.dy.memorygod.manager.PatternManager
import org.junit.Assert
import org.junit.Test

class PatternManagerTest {

    @Test
    fun testMarketVersion() {
        Assert.assertEquals(true, PatternManager.isMarketVersion("1.0.0"))
        Assert.assertEquals(true, PatternManager.isMarketVersion("1.99.0"))
        Assert.assertEquals(true, PatternManager.isMarketVersion("1.999.0"))
        Assert.assertEquals(true, PatternManager.isMarketVersion("1.0.99"))
        Assert.assertEquals(true, PatternManager.isMarketVersion("1.0.999"))
        Assert.assertEquals(true, PatternManager.isMarketVersion("99.0.0"))
        Assert.assertEquals(true, PatternManager.isMarketVersion("99.999.999"))

        Assert.assertEquals(false, PatternManager.isMarketVersion("1.1000.0"))
        Assert.assertEquals(false, PatternManager.isMarketVersion("1.0.1000"))
        Assert.assertEquals(false, PatternManager.isMarketVersion("100.0.0"))
        Assert.assertEquals(false, PatternManager.isMarketVersion("100.1000.1000"))

        Assert.assertEquals(false, PatternManager.isMarketVersion("1.0.a"))
        Assert.assertEquals(false, PatternManager.isMarketVersion("1.b.0"))
        Assert.assertEquals(false, PatternManager.isMarketVersion("c.0.0"))
        Assert.assertEquals(false, PatternManager.isMarketVersion("a.b.c"))
    }

}