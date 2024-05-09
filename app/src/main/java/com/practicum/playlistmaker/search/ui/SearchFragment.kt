package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.ui.models.UiEvent
import com.practicum.playlistmaker.search.ui.models.UiState
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter
    private lateinit var onTrackClickDebounce: (Pair<Track, Boolean>) -> Unit
    private val viewModel: SearchViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeClearTextEnabled().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeClearText().observe(viewLifecycleOwner) {
            renderClear()
        }

        /** Реализация отклика на нажатие элемента списка треков
         */

        onTrackClickDebounce = debounce<Pair<Track, Boolean>>(
            delayMillis = CLICK_DEBOUNCE_DELAY_MILLIS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false
        ) { itemInfo ->
            onTrackClick(itemInfo)
        }

        /** Параметры отображения истории поиска
         */

        searchHistoryAdapter = TrackListAdapter { track ->
            onTrackClickDebounce(Pair(track, false))
        }.apply {
            trackList = emptyList()
        }
        binding.searchHistoryRV.adapter = searchHistoryAdapter

        /** Параметры отображения результатов поиска
         */

        trackListAdapter = TrackListAdapter { track ->
            onTrackClickDebounce(Pair(track, true))
        }.apply {
            trackList = emptyList()
        }

        binding.tracklistRV.adapter = trackListAdapter

        /**
         * Реализация взаимодействия с полем ввода запроса поиска
         * Вызов отображения истории поиска
         * Автоматическая активация поиска
         */
        binding.inputEditText.run {
            setOnFocusChangeListener { _, hasFocus ->
                viewModel.onUiEvent(UiEvent.FocusChanged(hasFocus, this.text))
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.onUiEvent(UiEvent.ActionDone(this.text))
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
                    viewModel.onUiEvent(UiEvent.BeforeTextChanged(s))
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.onUiEvent(
                        UiEvent.OnTextChanged(
                            binding.inputEditText.hasFocus(),
                            s
                        )
                    )
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
        with(binding) {
            updateButton.setOnClickListener {
                viewModel.onUiEvent(UiEvent.UpdateButtonClick(inputEditText.text))
            }

            clearButton.setOnClickListener {
                viewModel.onUiEvent(UiEvent.ClearButtonClick)
            }

            clearHistoryButton.setOnClickListener {
                viewModel.onUiEvent(UiEvent.ClearHistoryButtonClick)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.uiStateFlow.collect {
                when (it) {
                    is UiState.Default -> hideAllViews()
                    is UiState.SearchHistory -> showSearchhistory(it.tracklist)
                    is UiState.ClearSearchHistory -> hideAllViews()
                    is UiState.Loading -> showLoading()
                    is UiState.SearchResult -> showSearchResult(it.tracklist)
                    is UiState.EmptyResult -> showEmptyResult()
                    is UiState.Error -> showOnFailure()
                }
            }
        }
    }

    private fun onTrackClick(itemInfo: Pair<Track, Boolean>) {
        val track = itemInfo.first
        val addTrackToHistory = itemInfo.second
        if (addTrackToHistory) viewModel.onUiEvent(UiEvent.AddTrack(track))
        goToPlayer(track)
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
            tracklistRV.apply {
                adapter?.notifyDataSetChanged()
                isVisible = true
                smoothScrollToPosition(0)
            }
            progressBar.isVisible = false
            inputEditText.clearFocus()
            viewModel.onUiEvent(UiEvent.ShowSearchResult(inputEditText.text))
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
            updateButton.isVisible = false
            viewModel.onUiEvent(UiEvent.ShowSearchResult(inputEditText.text))
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
            viewModel.onUiEvent(UiEvent.ShowSearchResult(inputEditText.text))
        }
    }

    override fun onStop() {
        viewModel.onUiEvent(UiEvent.OnStop)
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

    private fun showLoading() {
        hideAllViews()
        binding.progressBar.isVisible = true
        hideKeyboard(binding.inputEditText)
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

