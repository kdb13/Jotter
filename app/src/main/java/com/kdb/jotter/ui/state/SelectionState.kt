package com.kdb.jotter.ui.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class SelectionState(
    private val selectedIds: SnapshotStateList<Long> = mutableStateListOf()
) {
    val hasSelection get() = selectedIds.isEmpty().not()

    fun isItemSelected(id: Long) = selectedIds.contains(id)

    fun handleLongClick(id: Long) {
       if (!hasSelection) {
           selectedIds.add(id)
       }
    }

    fun handleClick(id: Long): Boolean {
        if (hasSelection) {
            if (selectedIds.contains(id)) {
                selectedIds.remove(id)
            } else {
                selectedIds.add(id)
            }

            return true
        }

        return false
    }
}
