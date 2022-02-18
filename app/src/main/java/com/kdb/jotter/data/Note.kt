package com.kdb.jotter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String = "",
    val dateCreated: Long = Date().time
)
