package com.poetcodes.googlekeepclone.utils

import junit.framework.Assert.assertEquals
import org.junit.Test

class ConstantsUtilTest {

    @Test
    fun constantsAreValid() {
        val databaseName = "notes_db"
        val noteExtra = "NOTE_EXTRA"
        val isGridPaperBook = "IS_GRID_PAPER_BOOK"
        assertEquals(databaseName, ConstantsUtil.DATABASE_NAME)
        assertEquals(noteExtra, ConstantsUtil.NOTE_EXTRA)
        assertEquals(isGridPaperBook, ConstantsUtil.IS_GRID_PAPER_BOOK)
    }
}