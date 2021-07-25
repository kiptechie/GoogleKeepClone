package com.poetcodes.googlekeepclone.utils.mappers

interface DateMapperInterface<String, Date> {

    fun toString(date: Date) : String

    fun toDate(dateString: String) : Date

}