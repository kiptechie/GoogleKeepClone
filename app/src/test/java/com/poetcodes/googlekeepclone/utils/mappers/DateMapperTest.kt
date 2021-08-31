package com.poetcodes.googlekeepclone.utils.mappers

import com.poetcodes.googlekeepclone.utils.GsonUtil
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateMapperTest {

    @Test
    fun dateMapperWorks() {
        val dateMapper: DateMapper = HelpersUtil.dateMapperInstance()
        val date = Date(System.currentTimeMillis())
        val actualDateString = dateMapper.toString(date)
        val expectedDateString = GsonUtil.gsonInstance().toJson(date)
        assertEquals(expectedDateString, actualDateString)
        val actualDate = dateMapper.toDate(expectedDateString)
        assertEquals(date.toString(), actualDate.toString())
    }

}