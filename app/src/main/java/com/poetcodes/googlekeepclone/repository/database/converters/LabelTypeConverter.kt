package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import com.poetcodes.googlekeepclone.utils.GsonUtil

class LabelTypeConverter {

    @TypeConverter
    fun fromLabel(label: Label?): String {
        var myLabel = label
        if (myLabel == null) {
            myLabel = Label(-1, "")
        }
        return GsonUtil.gsonInstance().toJson(myLabel)
    }

    @TypeConverter
    fun toLabel(labelString: String?): Label {
        var myLabelString = labelString
        if (myLabelString == null) {
            myLabelString = ""
        }
        val labelType = object : TypeToken<Label?>() {}.type
        return GsonUtil.gsonInstance().fromJson(myLabelString, labelType)
    }

}