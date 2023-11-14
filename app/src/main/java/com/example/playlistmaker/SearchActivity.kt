package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    var savedText: String? = ""

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService = retrofit.create(iTunesApiService::class.java)

    private var searchResults = ArrayList<Track>()

    lateinit var searchField: EditText
    lateinit var searchErrorPlaceholder: TextView
    lateinit var connectionErrorPlaceholder: TextView
    lateinit var refreshButton: Button
    lateinit var rvSearchResults: RecyclerView


    val searchResultsAdapter = SearchResultsAdapter(searchResults)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


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
                true
            }
            false

        }


        val clearButton = findViewById<ImageView>(R.id.iv_clearIcon)

        clearButton.setOnClickListener {
            searchField.setText("")
            searchResults.clear()
            searchErrorPlaceholder.visibility = View.GONE
            searchResultsAdapter.notifyDataSetChanged()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s.toString()
            }

        }
        searchField.addTextChangedListener(simpleTextWatcher)


        rvSearchResults = findViewById(R.id.rvSearchResults)
        rvSearchResults.adapter = searchResultsAdapter
        searchErrorPlaceholder = findViewById(R.id.tv_search_error_placeholder)
        connectionErrorPlaceholder = findViewById(R.id.tv_connection_error_placeholder)
        refreshButton = findViewById(R.id.search_refresh_btn)

        refreshButton.setOnClickListener { search() }

    }

    private fun search() {
        iTunesApiService.search(searchField.text.toString())
            .enqueue(object : Callback<SearchResultsResponse> {
                override fun onResponse(
                    call: Call<SearchResultsResponse>,
                    response: Response<SearchResultsResponse>
                ) {
                    if (response.code() == 200) {
                        searchResults.clear()
                        connectionErrorPlaceholder.visibility = View.GONE
                        refreshButton.visibility = View.GONE
                        if (response.body()?.results?.isNotEmpty() == true) {
                            searchResults.addAll(response.body()?.results!!)
                            searchResultsAdapter.notifyDataSetChanged()
                        }
                        if (searchResults.isEmpty()) {
                            searchErrorPlaceholder.visibility = View.VISIBLE
                        }

                    }
                }

                override fun onFailure(call: Call<SearchResultsResponse>, t: Throwable) {
                    connectionErrorPlaceholder.visibility = View.VISIBLE
                    refreshButton.visibility = View.VISIBLE

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


    }

}

