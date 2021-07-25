package com.poetcodes.googlekeepclone.ui

sealed class MainStateEvent {

    object FetchNotesEvents : MainStateEvent()
    object FetchArchivesEvents : MainStateEvent()
    object FetchDraftsEvents : MainStateEvent()
    object FetchTrashEvents : MainStateEvent()

    object None : MainStateEvent()

}