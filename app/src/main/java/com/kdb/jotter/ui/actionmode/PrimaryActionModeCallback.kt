package com.kdb.jotter.ui.actionmode

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.recyclerview.selection.SelectionTracker

/**
 * A convenience class for creating Primary Action Mode by
 * [Nick Banes](https://medium.com/over-engineering/using-androids-actionmode-e903181f2ee3).
 */
class PrimaryActionModeCallback(@MenuRes private val menuResId: Int, private val listener: OnActionItemClickListener) : ActionMode.Callback {

    private var actionMode: ActionMode? = null
    lateinit var tracker: SelectionTracker<Long>

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        this.actionMode = mode
        mode.menuInflater.inflate(menuResId, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode) {
        this.actionMode = null
        tracker.clearSelection()
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        listener.onActionItemClick(item)
        mode.finish()
        return true
    }

    /**
     * Starts the action mode, if it was not started.
     */
    fun start(view: View) {
        actionMode ?: view.startActionMode(this)
    }

    /**
     * Finishes the action mode.
     */
    fun finish() {
        actionMode?.finish()
    }

    fun updateTitle(title: String) {
        actionMode?.title = title
    }
}