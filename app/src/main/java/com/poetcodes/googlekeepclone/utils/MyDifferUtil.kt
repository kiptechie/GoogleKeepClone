package com.poetcodes.googlekeepclone.utils

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.poetcodes.googlekeepclone.repository.models.entities.Note

object MyDifferUtil {

    private val noteItemCallback: DiffUtil.ItemCallback<Note> =
        object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.createdAt == newItem.createdAt &&
                        oldItem.description == newItem.description &&
                        oldItem.title == newItem.title &&
                        oldItem.updatedAt == newItem.updatedAt &&
                        oldItem.label == newItem.label &&
                        oldItem.image == newItem.image &&
                        oldItem.images == newItem.images &&
                        oldItem.backgroundColor == newItem.backgroundColor
            }
        }

    var noteAsyncDifferConfig = AsyncDifferConfig.Builder(noteItemCallback)
        .build()

}