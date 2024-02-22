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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.SearchActivityState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchActivityViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var searchHistoryAdapter: SearchResultsAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed: Boolean = true
    private lateinit var viewModel: SearchActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchActivityViewModel.getViewModelFactory()
        )[SearchActivityViewModel::class.java]

        viewModel.getActivityState().observe(this) { state ->
            renderState(state)
        }

        val intent = Intent(this, AudioPlayerActivity::class.java)

        searchResultsAdapter = SearchResultsAdapter(viewModel.receiveSearchResults()) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        binding.rvSearchResults.adapter = searchResultsAdapter

        viewModel.getSearchResultsLiveData().observe(this) { searchResults ->
            searchResultsAdapter.setItems(searchResults)
        }


        searchHistoryAdapter = SearchResultsAdapter(viewModel.loadSearchHistory()) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        binding.rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        binding.rvSearchHistory.adapter = searchHistoryAdapter

        viewModel.getSearchHistoryLiveData().observe(this) { searchHistory ->
            searchHistoryAdapter.setItems(searchHistory)
        }


        binding.searchRefreshBtn.setOnClickListener {
            viewModel.searchDebounce(viewModel.getPreviousRequest())
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            hideSearchHistory()
        }

        binding.returnButton.setOnClickListener {
            this.finish()
        }


        binding.etSearchField.setText(viewModel.getPreviousRequest())
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
                if (hasFocus && binding.etSearchField.text.isEmpty() && viewModel.loadSearchHistory()
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
        }



        binding.ivClearIcon.setOnClickListener {
            binding.etSearchField.setText("")
            viewModel.clearSearchField()
            hideSearchResults()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClearIcon.visibility = clearButtonVisibility(s)
                binding.searchHistory.visibility =
                    if (binding.etSearchField.hasFocus() && viewModel.loadSearchHistory()
                            .isNotEmpty() && s?.isEmpty() == true
                    ) View.VISIBLE else View.GONE

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setPreviousRequest(s.toString())
                viewModel.searchDebounce(s.toString())
            }
        }
        binding.etSearchField.addTextChangedListener(simpleTextWatcher)

    }


    private fun renderState(state: SearchActivityState) {
        when (state) {
            is SearchActivityState.Default -> {
                hideAllErrorPlaceholders()
                hideProgressBar()
                hideSearchResults()
            }

            is SearchActivityState.Loading -> {
                showProgressBar()
            }

            is SearchActivityState.ShowResults -> {
                searchResultsAdapter.notifyDataSetChanged()
                showSearchResults()
            }

            is SearchActivityState.NoResultsFoundError -> {
                showSearchErrorPlaceholder()
            }

            is SearchActivityState.NoInternetConnectionError -> {
                showConnectionErrorPlaceholder()
            }

        }
    }


    private fun processClickedTrack(track: Track, intent: Intent) {
        viewModel.addTrackToSearchHistory(track)
        viewModel.selectTrackForPlayer(track)
        startActivity(intent)

    }


    private fun hideSearchHistory() {
        binding.searchHistory.isVisible = false
    }

    private fun showSearchResults() {
        hideProgressBar()
        hideAllErrorPlaceholders()
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
