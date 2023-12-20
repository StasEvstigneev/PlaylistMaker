package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY = "SearchHistory"
const val SEARCH_HISTORY_KEY = "SearchHistoryKey"
const val SEARCH_HISTORY_ITEMS_LIMIT: Int = 10
const val INTENT_KEY_FOR_TRACK = "track"

class SearchActivity : AppCompatActivity() {

    var savedText: String? = ""

    private val retrofit = Retrofit.Builder().baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val iTunesApiService = retrofit.create(iTunesApiService::class.java)

    private var searchResults = ArrayList<Track>()
    private var searchHistoryList = ArrayList<Track>()

    private lateinit var searchHistoryPrefs: SharedPreferences
    lateinit var searchField: EditText
    lateinit var searchErrorPlaceholder: TextView
    lateinit var connectionErrorPlaceholder: TextView
    lateinit var refreshButton: Button
    lateinit var rvSearchResults: RecyclerView
    lateinit var searchHistoryGroup: LinearLayout
    private lateinit var tvYouSearchedFor: TextView
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var btnClearHistory: Button
    lateinit var searchResultsAdapter: SearchResultsAdapter
    lateinit var searchHistoryAdapter: SearchResultsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryPrefs = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        val searchHistory = SearchHistory(searchHistoryPrefs)
        searchHistoryList = searchHistory.getSearchHistory()

        searchResultsAdapter = SearchResultsAdapter(searchResults)
        rvSearchResults = findViewById(R.id.rvSearchResults)
        rvSearchResults.adapter = searchResultsAdapter

        searchHistoryAdapter = SearchResultsAdapter(searchHistoryList)
        rvSearchHistory = findViewById(R.id.rvSearchHistory)
        rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        rvSearchHistory.adapter = searchHistoryAdapter

        searchErrorPlaceholder = findViewById(R.id.tv_search_error_placeholder)
        connectionErrorPlaceholder = findViewById(R.id.tv_connection_error_placeholder)
        refreshButton = findViewById(R.id.search_refresh_btn)
        refreshButton.setOnClickListener { search() }

        searchHistoryGroup = findViewById(R.id.searchHistory)
        tvYouSearchedFor = findViewById(R.id.tvYouSearchedFor)

        btnClearHistory = findViewById(R.id.btnClearHistory)
        btnClearHistory.setOnClickListener {
            searchHistoryList.clear()
            searchHistory.clearSearchHistory()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryGroup.isVisible = false

        }

        val buttonReturn = findViewById<ImageView>(R.id.return_from_settings)
        buttonReturn.setOnClickListener {
            this.finish()
        }

        searchField = findViewById(R.id.et_search_field)
        searchField.setText(savedText)
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (searchField.text.isNotEmpty()) {
                    search()
                }
            }
            false
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryGroup.visibility =
                if (hasFocus && searchField.text.isEmpty() && searchHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val clearButton = findViewById<ImageView>(R.id.iv_clearIcon)

        clearButton.setOnClickListener {
            searchField.setText("")
            searchResults.clear()
            searchErrorPlaceholder.isVisible = false
            searchResultsAdapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchHistoryGroup.visibility =
                    if (searchField.hasFocus() && searchHistoryList.isNotEmpty() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s.toString()
            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)
        val intent = Intent(this, AudioPlayerActivity::class.java)

        searchResultsAdapter.setOnClickListener(object : SearchResultsAdapter.OnTrackClickListener {
            override fun onTrackClick(track: Track) {
                searchHistoryList = searchHistory.addNewElement(track, searchHistoryAdapter)
                searchHistoryAdapter.notifyDataSetChanged()
                intent.apply { putExtra(INTENT_KEY_FOR_TRACK, track) }
                startActivity(intent)
            }
        })

        searchHistoryAdapter.setOnClickListener(object : SearchResultsAdapter.OnTrackClickListener {
            override fun onTrackClick(track: Track) {
                searchHistoryList = searchHistory.addNewElement(track, searchHistoryAdapter)
                intent.apply { putExtra(INTENT_KEY_FOR_TRACK, track) }
                startActivity(intent)
            }
        })
    }

    private fun search() {
        iTunesApiService.search(searchField.text.toString())
            .enqueue(object : Callback<SearchResultsResponse> {
                override fun onResponse(
                    call: Call<SearchResultsResponse>, response: Response<SearchResultsResponse>
                ) {
                    if (response.code() == 200) {
                        searchResults.clear()
                        rvSearchResults.isVisible = true
                        connectionErrorPlaceholder.isVisible = false
                        refreshButton.isVisible = false
                        searchErrorPlaceholder.isVisible = false
                        if (response.body()?.results?.isNotEmpty() == true) {
                            searchResults.addAll(response.body()?.results!!)
                            searchResultsAdapter.notifyDataSetChanged()
                        }
                        if (searchResults.isEmpty()) {
                            rvSearchResults.isVisible = false
                            connectionErrorPlaceholder.isVisible = false
                            refreshButton.isVisible = false
                            searchErrorPlaceholder.isVisible = true
                        }

                    } else {
                        searchResults.clear()
                        rvSearchResults.isVisible = false
                        connectionErrorPlaceholder.isVisible = false
                        refreshButton.isVisible = false
                        searchErrorPlaceholder.isVisible = true

                    }
                }

                override fun onFailure(call: Call<SearchResultsResponse>, t: Throwable) {
                    searchResults.clear()
                    rvSearchResults.isVisible = false
                    searchErrorPlaceholder.isVisible = false
                    connectionErrorPlaceholder.isVisible = true
                    refreshButton.isVisible = true
                }
            }
            )

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
        const val SAVED_TEXT = "SavedText"
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

}
