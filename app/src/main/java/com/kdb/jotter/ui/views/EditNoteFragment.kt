package com.kdb.jotter.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kdb.jotter.JotterApplication
import com.kdb.jotter.R
import com.kdb.jotter.databinding.FragmentEditNoteBinding
import com.kdb.jotter.ui.clearFocusOnBack
import com.kdb.jotter.ui.showSoftKeyboard
import com.kdb.jotter.ui.viewmodels.EditNoteViewModel
import com.kdb.jotter.ui.viewmodels.EditNoteViewModelFactory

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val args: EditNoteFragmentArgs by navArgs()

    private val viewModel: EditNoteViewModel by viewModels {
        EditNoteViewModelFactory(
            (activity?.application as JotterApplication).repository,
            args.noteID
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable options menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@EditNoteFragment.viewModel

            // Clear focus when Back key is pressed in EditText
            editTextNoteContent.clearFocusOnBack()
        }

        // Show the keyboard when adding a new note
        if (viewModel.isNewNote) {
            binding.editTextNoteContent.showSoftKeyboard()
        }
    }

    override fun onStop() {
        super.onStop()

        // Save note
        viewModel.saveNote()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteNoteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_note_message))
            .setPositiveButton(getString(R.string.button_delete), { _, _ -> deleteNote() })
            .setNegativeButton(getString(R.string.button_cancel), null)
            .setCancelable(false)
            .show()
    }

    private fun deleteNote() {
        // Delete the current note
        viewModel.deleteNote()

        // Navigate back
        findNavController().navigateUp()
    }

    private fun shareNote() {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, viewModel.content.value)
        }

        val chooserIntent = Intent.createChooser(sendIntent, "Share note")

        if (sendIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooserIntent)
        }
    }

    /*
        OPTIONS MENU
        ------------
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteNoteDialog()
                true
            }

            R.id.action_share -> {
                shareNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}