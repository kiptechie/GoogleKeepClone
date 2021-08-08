package com.poetcodes.googlekeepclone.repository.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Images(
    val images : List<String>
) : Parcelable
