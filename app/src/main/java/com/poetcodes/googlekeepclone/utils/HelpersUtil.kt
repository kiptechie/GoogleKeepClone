package com.poetcodes.googlekeepclone.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import com.blankj.utilcode.util.SnackbarUtils
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.utils.mappers.DateMapper
import java.util.*

object HelpersUtil {

    private var dateMapper: DateMapper? = null

    @JvmStatic
    fun dateMapperInstance(): DateMapper {
        if (dateMapper == null) {
            dateMapper = DateMapper()
        }
        return dateMapper as DateMapper
    }

    @JvmStatic
    fun showBottomSnack(view: View, message: String?) {
        if (message != null) {
            SnackbarUtils.with(view)
                .setMessage(message)
                .setAction("Dismiss") {
                    SnackbarUtils.dismiss()
                }
                .setDuration(SnackbarUtils.LENGTH_INDEFINITE)
                .show()
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(
                { SnackbarUtils.dismiss() },
                2500
            )
        }
    }

    @JvmStatic
    fun isNewNote(note: Note?): Boolean {
        if (note == null) {
            return true
        }
        return note.title == "" && note.description == ""
    }

    @JvmStatic
    fun newNote(): Note {
        val title = ""
        val description = ""
        val currentTime = System.currentTimeMillis().toString()
        val id = UUID.randomUUID().toString()
        val noteEssentials = NoteEssentials(
            id,
            title,
            description,
            currentTime,
            currentTime
        )
        val noteEntityUtil = NoteEntityUtil.Builder()
            .withNoteEssentials(noteEssentials)
            .build()
        return noteEntityUtil.note
    }

}