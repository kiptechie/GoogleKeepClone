package com.poetcodes.googlekeepclone.utils

import com.google.gson.Gson

object GsonUtil {

    private var gson: Gson? = null

    @JvmStatic
    fun gsonInstance(): Gson {
        if (gson == null) {
            gson = Gson()
        }
        return gson as Gson
    }
}