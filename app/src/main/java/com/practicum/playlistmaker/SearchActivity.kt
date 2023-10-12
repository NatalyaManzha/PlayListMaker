package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
    }

    private lateinit var searchRequest: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val inputEditText = findViewById<EditText>(R.id.search_input)
        if (savedInstanceState != null)
            inputEditText.setText(savedInstanceState.getString(SEARCH_REQUEST, ""))

        val clearButton = findViewById<ImageView>(R.id.clearButton)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }
        inputEditText.addTextChangedListener(textWatcher)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
    }
}

private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

