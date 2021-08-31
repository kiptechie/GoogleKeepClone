package com.poetcodes.googlekeepclone.utils

import junit.framework.Assert.assertEquals
import org.junit.Test

class GsonUtilTest {

    @Test
    fun gsonUtilInitialized() {
        val gsonUtil = GsonUtil.gsonInstance()
        assertEquals(gsonUtil, GsonUtil.gsonInstance())
    }
}