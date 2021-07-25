package com.poetcodes.googlekeepclone.ui

sealed class MainStateEvent {

    object NoteEvents : MainStateEvent()
    object ArchiveEvents : MainStateEvent()
    object DraftEvents : MainStateEvent()
    object TrashEvents : MainStateEvent()
    object LabelEvents : MainStateEvent()

    object None : MainStateEvent()

}