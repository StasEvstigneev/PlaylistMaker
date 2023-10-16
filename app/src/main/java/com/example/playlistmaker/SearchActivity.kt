package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    var savedText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val buttonReturn = findViewById<ImageView>(R.id.return_from_settings)
        buttonReturn.setOnClickListener {
            this.finish()
        }

        val editText = findViewById<EditText>(R.id.et_search_field)
        editText.setText(savedText)


        val clearButton = findViewById<ImageView>(R.id.iv_clearIcon)

        clearButton.setOnClickListener {
            editText.setText("")
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
        editText.addTextChangedListener(simpleTextWatcher)

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

