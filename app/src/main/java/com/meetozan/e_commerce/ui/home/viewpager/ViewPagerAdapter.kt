package com.meetozan.e_commerce.ui.home.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.meetozan.e_commerce.ui.cosmetic.CosmeticFragment
import com.meetozan.e_commerce.ui.electronic.ElectronicFragment
import com.meetozan.e_commerce.ui.fashion.FashionFragment
import com.meetozan.e_commerce.ui.household.HouseholdFragment
import com.meetozan.e_commerce.ui.man.ManFragment
import com.meetozan.e_commerce.ui.newest.NewestFragment
import com.meetozan.e_commerce.ui.woman.WomanFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: androidx.lifecycle.Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> NewestFragment()
        1 -> ManFragment()
        2 -> WomanFragment()
        3 -> FashionFragment()
        4 -> ElectronicFragment()
        5 -> HouseholdFragment()
        6 -> CosmeticFragment()
        else -> NewestFragment()
    }
}