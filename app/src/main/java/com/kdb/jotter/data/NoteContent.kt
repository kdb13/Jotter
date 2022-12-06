package com.kdb.jotter.data

data class NoteContent(
    val id: Long,
    val title: String? = null,
    val content: String
)
