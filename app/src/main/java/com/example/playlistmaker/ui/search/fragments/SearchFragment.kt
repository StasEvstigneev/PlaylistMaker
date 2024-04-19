package com.example.playlistmaker.ui.search.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.SearchState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.AudioPlayerActivity
import com.example.playlistmaker.ui.search.TracksAdapter
import com.example.playlistmaker.presentation.search.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var textWatcher: TextWatcher

    private lateinit var searchResultsAdapter: TracksAdapter
    private lateinit var searchHistoryAdapter: TracksAdapter

    private var isClickAllowed: Boolean = true
    private var isSearchHistoryAvailable = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getActivityState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)

        searchResultsAdapter = TracksAdapter(ArrayList<Track>()) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        binding.rvSearchResults.adapter = searchResultsAdapter

        searchHistoryAdapter = TracksAdapter(ArrayList<Track>()) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        binding.rvSearchHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        binding.rvSearchHistory.adapter = searchHistoryAdapter


        binding.searchRefreshBtn.setOnClickListener {
            viewModel.repeatRequest()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            isSearchHistoryAvailable = false
            hideSearchHistory()
        }


        binding.etSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (binding.etSearchField.text.isNotEmpty()) {
                    viewModel.searchDebounce(binding.etSearchField.text.toString())
                }
            }
            false
        }

        binding.etSearchField.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistory.visibility =
                if (hasFocus && binding.etSearchField.text.isEmpty() && isSearchHistoryAvailable
                ) View.VISIBLE else View.GONE
        }

        binding.ivClearIcon.setOnClickListener {
            binding.etSearchField.setText("")
            viewModel.clearSearchField()
            hideAllErrorPlaceholders()
            hideSearchResults()
        }


        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClearIcon.visibility = clearButtonVisibility(s)
                binding.searchHistory.visibility =
                    if (binding.etSearchField.hasFocus()
                        && !binding.tvSearchErrorPlaceholder.isVisible
                        && !binding.tvConnectionErrorPlaceholder.isVisible
                        && isSearchHistoryAvailable && s?.isEmpty() == true
                    ) View.VISIBLE else View.GONE

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.searchDebounce(s.toString() ?: "")

            }
        }
        textWatcher?.let { binding.etSearchField.addTextChangedListener(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { binding.etSearchField.removeTextChangedListener(it) }
        _binding = null

    }


    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.Default -> {
                searchHistoryAdapter.list = state.searchHistory
                searchHistoryAdapter.notifyDataSetChanged()
                searchResultsAdapter.list = state.searchResults
                searchResultsAdapter.notifyDataSetChanged()

                isSearchHistoryAvailable = state.searchHistory.isNotEmpty()

                if (state.searchResults.isNotEmpty()) {
                    showSearchResults()
                } else if (state.searchHistory.isNotEmpty()) {
                    showSearchHistory()
                }

                hideAllErrorPlaceholders()
                hideProgressBar()
            }

            is SearchState.Loading -> {
                searchResultsAdapter.list = state.searchResults
                searchResultsAdapter.notifyDataSetChanged()
                showProgressBar()
            }

            is SearchState.ShowSearchResults -> {
                searchResultsAdapter.list = state.searchResults
                searchResultsAdapter.notifyDataSetChanged()
                showSearchResults()
            }

            is SearchState.NoResultsFoundError -> {
                showSearchErrorPlaceholder()
            }

            is SearchState.NoInternetConnectionError -> {
                showConnectionErrorPlaceholder()
            }

        }
    }

    private fun processClickedTrack(track: Track, intent: Intent) {
        viewModel.addTrackToSearchHistory(track)
        viewModel.playThisTrack(track)
        startActivity(intent)

    }

    private fun showSearchHistory() {
        binding.searchHistory.isVisible = true
        hideSearchResults()
    }


    private fun hideSearchHistory() {
        binding.searchHistory.isVisible = false
    }

    private fun showSearchResults() {
        hideProgressBar()
        hideAllErrorPlaceholders()
        hideSearchHistory()
        binding.rvSearchResults.isVisible = true

    }

    private fun hideSearchResults() {
        binding.rvSearchResults.isVisible = false
    }

    private fun showSearchErrorPlaceholder() {
        hideConnectionErrorPlaceholder()
        hideSearchHistory()
        hideProgressBar()
        binding.tvSearchErrorPlaceholder.isVisible = true

    }

    private fun hideSearchErrorPlaceholder() {
        binding.tvSearchErrorPlaceholder.isVisible = false
    }

    private fun showConnectionErrorPlaceholder() {
        hideProgressBar()
        hideSearchErrorPlaceholder()
        binding.tvConnectionErrorPlaceholder.isVisible = true
        binding.searchRefreshBtn.isVisible = true
    }

    private fun hideConnectionErrorPlaceholder() {
        binding.tvConnectionErrorPlaceholder.isVisible = false
        binding.searchRefreshBtn.isVisible = false
    }

    private fun hideAllErrorPlaceholders() {
        hideSearchErrorPlaceholder()
        hideConnectionErrorPlaceholder()
    }


    private fun showProgressBar() {
        binding.searchProgressBar.isVisible = true
        hideSearchResults()
        hideAllErrorPlaceholders()
    }

    private fun hideProgressBar() {
        binding.searchProgressBar.isVisible = false
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun clickDebounce(): Boolean {
        val clickStatus = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(ON_TRACK_CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return clickStatus
    }


    companion object {
        private const val ON_TRACK_CLICK_DELAY = 2000L
    }

}