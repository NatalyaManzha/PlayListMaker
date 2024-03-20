package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.ui.models.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController

class SearchFragment : BindingFragment<FragmentSearchBinding>() {


    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter
    private val viewModel: SearchViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Default -> hideAllViews()
                is UiState.SearchHistory -> showSearchhistory(it.tracklist)
                is UiState.ClearSearchHistory -> onClearedSearchHistory()
                is UiState.Loading -> showLoading()
                is UiState.SearchResult -> showSearchResult(it.tracklist)
                is UiState.EmptyResult -> showEmptyResult()
                is UiState.Error -> showOnFailure()
            }
        }
        viewModel.observeClearTextEnabled().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeClearText().observe(viewLifecycleOwner) {
            renderClear()
        }
        /**
         * Параметры отображения истории поиска
         * Реализация отклика на нажатие элемента списка истории поиска
         */
        searchHistoryAdapter = TrackListAdapter { track ->
            if (clickDebounce()) goToPlayer(track)
        }.apply {
            trackList = emptyList()
        }
        binding.searchHistoryRV.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchHistoryAdapter
        }

        /**
         * Параметры отображения результатов поиска
         * Реализация отклика на нажатие элемента списка результатов поиска
         * и обновлениение истории поиска
         */
        trackListAdapter = TrackListAdapter { track ->
            if (clickDebounce()) {
                viewModel.addTrack(track)
                goToPlayer(track)
            }
        }.apply {
            trackList = emptyList()
        }

        binding.tracklistRV.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = trackListAdapter
        }

        /**
         * Реализация взаимодействия с полем ввода запроса поиска
         * Вызов отображения истории поиска
         * Автоматическая активация поиска
         */
        binding.inputEditText.run {
            setOnFocusChangeListener { _, hasFocus ->
                viewModel.showSearchhistory(hasFocus, binding.inputEditText.text)
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.startSearch()
                    true
                }
                false
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    viewModel.beforeTextChanged(s)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    with(binding) {
                        viewModel.onTextChanged(s)
                        viewModel.showSearchhistory(inputEditText.hasFocus(), s)
                        viewModel.searchDebounce()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        binding.updateButton.setOnClickListener {
            viewModel.startSearch()
        }

        binding.clearButton.setOnClickListener {
            viewModel.onTextCleared()
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    private fun goToPlayer(track: Track) {
        findNavController().navigate(
            R.id.actionSearchFragmentToPlayerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun showSearchResult(trackList: List<Track>) {
        with(binding) {
            searchHistoryLayout.isVisible = false
            trackListAdapter.trackList = trackList
            tracklistRV.adapter?.notifyDataSetChanged()
            tracklistRV.isVisible = true
            progressBar.isVisible = false
            inputEditText.clearFocus()
            viewModel.beforeTextChanged(inputEditText.text.toString())
        }
    }

    private fun showEmptyResult() {
        with(binding) {
            placeholderImage.isVisible = true
            placeholderImage.setImageResource(R.drawable.search_failed)
            placeholderMessage.isVisible = true
            placeholderMessage.setText(R.string.search_failed)
            progressBar.isVisible = false
            inputEditText.clearFocus()
            updateButton.isVisible = true
            viewModel.beforeTextChanged(inputEditText.text.toString())
        }
    }

    private fun showOnFailure() {
        with(binding) {
            tracklistRV.isVisible = false
            placeholderImage.isVisible = true
            placeholderImage.setImageResource(R.drawable.no_internet)
            placeholderMessage.isVisible = true
            placeholderMessage.setText(R.string.no_internet)
            updateButton.isVisible = true
            progressBar.isVisible = false
            inputEditText.clearFocus()
            viewModel.beforeTextChanged(inputEditText.text.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPause()
    }

    override fun onStop() {
        viewModel.saveSearchHistory()
        super.onStop()
    }

    private fun render(enabled: Boolean) {
        binding.clearButton.isVisible = enabled
    }

    private fun renderClear() {
        binding.inputEditText.setText(EDIT_TEXT_DEFAULT)
        binding.inputEditText.clearFocus()
        hideAllViews()
        hideKeyboard(binding.inputEditText)
    }

    private fun showSearchhistory(trackList: List<Track>) {
        hideAllViews()
        searchHistoryAdapter.trackList = trackList
        binding.searchHistoryRV.adapter?.notifyDataSetChanged()
        binding.searchHistoryLayout.isVisible = true
        binding.searchHistoryRV.smoothScrollToPosition(0)
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    private fun showLoading() {
        hideAllViews()
        binding.progressBar.isVisible = true
        hideKeyboard(binding.inputEditText)
    }

    private fun onClearedSearchHistory() {
        hideAllViews()
    }

    private fun hideAllViews() {
        with(binding) {
            updateButton.isVisible = false
            placeholderImage.isVisible = false
            placeholderMessage.isVisible = false
            tracklistRV.isVisible = false
            clearButton.isVisible = false
            progressBar.isVisible = false
            searchHistoryLayout.isVisible = false
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val EDIT_TEXT_DEFAULT = ""
    }
}

