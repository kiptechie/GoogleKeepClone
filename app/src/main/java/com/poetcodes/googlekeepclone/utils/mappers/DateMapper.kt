package com.poetcodes.googlekeepclone.utils.mappers

import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.utils.GsonUtil
import java.util.*

class DateMapper : DateMapperInterface<String, Date> {

    override fun toString(date: Date): String {
        return GsonUtil.gsonInstance().toJson(date)
    }

    override fun toDate(dateString: String): Date {
        val dateType = object : TypeToken<Date?>() {}.type
        return GsonUtil.gsonInstance().fromJson(dateString, dateType)
    }

}