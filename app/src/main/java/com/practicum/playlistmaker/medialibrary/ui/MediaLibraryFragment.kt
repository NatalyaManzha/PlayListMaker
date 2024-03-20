package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : BindingFragment<FragmentMediaLibraryBinding>() {
    private lateinit var tabMediator: TabLayoutMediator
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaLibraryBinding {
        return FragmentMediaLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.favorites)
                    1 -> tab.text = resources.getString(R.string.playlists)
                }
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}
