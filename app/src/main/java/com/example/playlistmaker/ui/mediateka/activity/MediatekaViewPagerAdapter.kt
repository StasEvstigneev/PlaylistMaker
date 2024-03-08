package com.example.playlistmaker.ui.mediateka.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.mediateka.fragments.PlaylistsFragment
import com.example.playlistmaker.ui.mediateka.fragments.FavoriteTracksFragment


class MediatekaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return TABS_QUANTITY
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoriteTracksFragment.newInstance() else PlaylistsFragment.newInstance()
    }


    companion object {
        private const val TABS_QUANTITY = 2

    }


}