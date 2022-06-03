package com.example.todoish.ui.components

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.todoish.R
import com.example.todoish.databinding.MainFragmentBinding
import com.example.todoish.ui.utils.TodoStateAdapter
import com.example.todoish.ui.utils.TodosAdapter
import com.example.todoish.ui.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var viewDataBinding: MainFragmentBinding

    private lateinit var todoStateAdapter: TodoStateAdapter
    private lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = MainFragmentBinding.inflate(inflater, container, false).apply {
                this.viewmodel = viewModel
            }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Use the ViewModel

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupAppbar()
        setupNavigation()
        val tabLayout = viewDataBinding.tabs

        todoStateAdapter = TodoStateAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = todoStateAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                1 ->
                    tab.text = "Upcoming"
                2 ->
                    tab.text = "Task Done"
                3 ->
                    tab.text = "Failed"
                else ->
                    tab.text = "Today"
            }
        }.attach()
        // display necessary fragment when tabs changes

    }

    private fun setupAppbar() {
        viewDataBinding.appBar.title = viewModel.formatDate(viewModel.today.value, "MMMM dd, yyyy")
        viewDataBinding.appBar.subtitle = "Today"
    }

    private fun setupNavigation() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        viewDataBinding.appBar.setupWithNavController(navController, appBarConfiguration)

        viewDataBinding.appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            if (it.itemId != R.id.addTodo) {
                true
            } else {
                findNavController().navigate(R.id.action_mainFragment_to_todoFormFragment)
                true
            }
        })
    }
}


