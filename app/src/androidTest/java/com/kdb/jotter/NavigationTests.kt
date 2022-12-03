package com.kdb.jotter

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kdb.jotter.ui.views.NotesFragment
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTests {

    private lateinit var navController: NavController

    @Before
    fun setupNavigation() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val notesFragmentScenario =
            launchFragmentInContainer<NotesFragment>(themeResId = R.style.Theme_Jotter)

        notesFragmentScenario.onFragment {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }

    @Test
    fun navigate_to_add_note_nav_component() {
        // Perform the navigation test
        onView(withId(R.id.fab_add_note))
            .perform(click())

        // Test whether the destination is correct
        assertEquals(navController.currentDestination?.id, R.id.editNoteFragment)
    }

    @Test
    fun navigate_to_edit_note_nav_component() {
        // Click a note
        val position = 1

        onView(withId(R.id.recycler_view_notes))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    click()
                )
            )

        // Test whether the destination is correct
        assertEquals(navController.currentDestination?.id, R.id.editNoteFragment)
    }

}