package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.SearchState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var searchHistoryAdapter: SearchResultsAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed: Boolean = true
    private var isSearchHistoryAvailable = false


    private val viewModel by viewModel<SearchViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getActivityState().observe(this) { state ->
            renderState(state)
        }

        val intent = Intent(this, AudioPlayerActivity::class.java)

        searchResultsAdapter = SearchResultsAdapter(ArrayList<Track>()) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        binding.rvSearchResults.adapter = searchResultsAdapter

        searchHistoryAdapter = SearchResultsAdapter(ArrayList<Track>()) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        binding.rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        binding.rvSearchHistory.adapter = searchHistoryAdapter

        binding.searchRefreshBtn.setOnClickListener {
            viewModel.repeatRequest()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            isSearchHistoryAvailable = false
            hideSearchHistory()
        }

        binding.returnButton.setOnClickListener {
            this.finish()
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

        val simpleTextWatcher = object : TextWatcher {
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
        binding.etSearchField.addTextChangedListener(simpleTextWatcher)

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
        viewModel.selectTrackForPlayer(track)
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
            handler.postDelayed(
                { isClickAllowed = true },
                SEARCH_RESULTS_CLICK_DELAY
            )
        }
        return clickStatus
    }

    companion object {
        private const val SEARCH_RESULTS_CLICK_DELAY = 2000L
    }

}
