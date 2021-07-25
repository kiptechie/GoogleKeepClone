package com.poetcodes.googlekeepclone.utils.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DateMapper : DateMapperInterface<String, Date> {

    override fun toString(date: Date): String {
        return gson.toJson(date)
    }

    override fun toDate(dateString: String): Date {
        val dateType = object : TypeToken<Date?>() {}.type
        return gson.fromJson(dateString, dateType)
    }

    companion object {
        val gson = Gson()
    }

}