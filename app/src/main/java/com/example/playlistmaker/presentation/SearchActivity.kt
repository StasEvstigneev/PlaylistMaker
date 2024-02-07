package com.example.playlistmaker.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecase.audioplayer.SelectTrackForPlayerUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.AddElementToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.ClearSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.GetSearchHistoryUseCase


class SearchActivity : AppCompatActivity() {

    private val tracksInteractor = Creator.provideTracksInteractor()
    private lateinit var searchHistoryRepository: SearchHistoryRepository
    private lateinit var gsonJsonConverter: GsonJsonConverter
    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase
    private lateinit var addElementToSearchHistoryUseCase: AddElementToSearchHistoryUseCase
    private lateinit var clearSearchHistoryUseCase: ClearSearchHistoryUseCase
    private lateinit var selectTrackForPlayerUseCase: SelectTrackForPlayerUseCase

    var savedText: String? = ""

    private var searchResultsList = ArrayList<Track>()
    private var searchHistoryList = ArrayList<Track>()

    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchErrorPlaceholder: TextView
    private lateinit var connectionErrorPlaceholder: TextView
    private lateinit var refreshButton: Button
    lateinit var rvSearchResults: RecyclerView
    lateinit var searchHistoryGroup: LinearLayout
    private lateinit var tvYouSearchedFor: TextView
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var btnClearHistory: Button

    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var searchHistoryAdapter: SearchResultsAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if (searchField.text.isNotBlank()) search() }

    private var isClickAllowed: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryRepository = Creator.provideSearchHistoryRepository(context = applicationContext)
        gsonJsonConverter = Creator.provideGsonJsonConverter()
        getSearchHistoryUseCase = Creator.provideGetSearchHistoryUseCase(searchHistoryRepository, gsonJsonConverter)
        addElementToSearchHistoryUseCase = Creator.provideAddElementToSearchHistoryUseCase(searchHistoryRepository, getSearchHistoryUseCase, gsonJsonConverter)
        clearSearchHistoryUseCase = Creator.provideClearSearchHistoryUseCase(searchHistoryRepository)
        selectTrackForPlayerUseCase = Creator.provideSelectTrackForPlayerUseCase(searchHistoryRepository, gsonJsonConverter)

        searchHistoryList = getSearchHistoryUseCase.execute()

        searchField = findViewById(R.id.et_search_field)
        clearButton = findViewById(R.id.iv_clearIcon)

        progressBar = findViewById(R.id.searchProgressBar)
        rvSearchResults = findViewById(R.id.rvSearchResults)

        tvYouSearchedFor = findViewById(R.id.tvYouSearchedFor)
        rvSearchHistory = findViewById(R.id.rvSearchHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        searchHistoryGroup = findViewById(R.id.searchHistory)

        searchErrorPlaceholder = findViewById(R.id.tv_search_error_placeholder)
        connectionErrorPlaceholder = findViewById(R.id.tv_connection_error_placeholder)
        refreshButton = findViewById(R.id.search_refresh_btn)

        val intent = Intent(this, AudioPlayerActivity::class.java)

        searchResultsAdapter = SearchResultsAdapter(searchResultsList) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        rvSearchResults.adapter = searchResultsAdapter


        searchHistoryAdapter = SearchResultsAdapter(searchHistoryList) {
            if (clickDebounce()) {
                processClickedTrack(it, intent)
            }
        }
        rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        rvSearchHistory.adapter = searchHistoryAdapter

        refreshButton.setOnClickListener { search() }

        btnClearHistory.setOnClickListener {
            clearSearchHistoryUseCase.execute()
            searchHistoryAdapter.setItems(getSearchHistoryUseCase.execute())
            searchHistoryList = getSearchHistoryUseCase.execute()
            searchHistoryGroup.isVisible = false
        }

        val buttonReturn = findViewById<ImageView>(R.id.return_from_settings)
        buttonReturn.setOnClickListener {
            this.finish()
        }


        searchField.setText(savedText)
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (searchField.text.isNotEmpty()) {
                    searchDebounce()
                }
            }
            false
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryGroup.visibility =
                if (hasFocus && searchField.text.isEmpty() && searchHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        }



        clearButton.setOnClickListener {
            searchField.setText("")
            searchResultsList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            searchResultsAdapter.notifyItemRangeChanged(0, searchResultsList.size-1)
            searchErrorPlaceholder.isVisible = false
            rvSearchResults.isVisible = false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchDebounce()
                searchHistoryGroup.visibility =
                    if (searchField.hasFocus() && searchHistoryList.isNotEmpty() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s.toString()

            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)

    }


    private fun search() {

        showProgressBar()
        tracksInteractor.searchTracks(searchField.text.toString(), object: TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<Track>) {
                handler.post {
                    hideProgressBar()
                    searchResultsList.clear()
                    searchResultsAdapter.notifyDataSetChanged()
                    searchResultsAdapter.notifyItemRangeChanged(0, searchResultsList.size-1)
                    searchResultsList.addAll(foundTracks)
                    if (searchResultsList.isEmpty()) {
                        showSearchErrorPlaceholder()
                    } else {
                        hideAllErrorPlaceholders()
                        searchResultsAdapter.notifyDataSetChanged()
                        rvSearchResults.isVisible = true

                    }
                }
            }
        }
        )
    }

    private fun processClickedTrack(track: Track, intent: Intent) {
        addElementToSearchHistoryUseCase.execute(track)
        searchHistoryAdapter.setItems(getSearchHistoryUseCase.execute())
        selectTrackForPlayerUseCase.execute(track)
        searchHistoryList = getSearchHistoryUseCase.execute()
        startActivity(intent)

    }

    private fun showSearchErrorPlaceholder() {
        searchErrorPlaceholder.isVisible = true
    }

    private fun hideSearchErrorPlaceholder() {
        searchErrorPlaceholder.isVisible = false
    }


    private fun showConnectionErrorPlaceholder() {
        connectionErrorPlaceholder.isVisible = true
        refreshButton.isVisible = true
    }

    private fun hideConnectionErrorPlaceholder() {
        connectionErrorPlaceholder.isVisible = false
        refreshButton.isVisible = false
    }

    private fun hideAllErrorPlaceholders() {
        hideSearchErrorPlaceholder()
        hideConnectionErrorPlaceholder()
    }


    private fun showProgressBar() {
        progressBar.isVisible = true
        rvSearchResults.isVisible = false
        hideAllErrorPlaceholders()
    }

    private fun hideProgressBar() {
        progressBar.isVisible = false
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val clickStatus = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, SEARCH_RESULTS_CLICK_DELAY)
        }
        return clickStatus
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(SAVED_TEXT)
    }

    companion object {
        private const val SAVED_TEXT = "SavedText"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_RESULTS_CLICK_DELAY = 1000L
    }

}
