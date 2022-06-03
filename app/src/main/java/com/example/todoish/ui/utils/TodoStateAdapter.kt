package com.example.todoish.ui.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoish.ui.components.tabs.TaskDoneFragment
import com.example.todoish.ui.components.tabs.TaskFailedFragment
import com.example.todoish.ui.components.tabs.TodayFragment
import com.example.todoish.ui.components.tabs.UpcomingFragment

class TodoStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment = when (position) {
            1 -> UpcomingFragment()
            2 -> TaskDoneFragment()
            3 -> TaskFailedFragment()
            else -> TodayFragment()
        }

        return fragment
    }
}