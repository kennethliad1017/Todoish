package com.example.todoish.ui.components


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.todoish.R
import com.example.todoish.databinding.FragmentTodoFormBinding
import com.example.todoish.ui.EventObserver
import com.example.todoish.ui.viewmodel.FormViewModel
import com.example.todoish.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TodoFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TodoFormFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private val formViewModel: FormViewModel by viewModels()

    // argument pass from other destination
    private val args: TodoFormFragmentArgs by navArgs()

    private lateinit var viewDataBinding: FragmentTodoFormBinding

    // navigation
    private lateinit var navController: NavController


    companion object {
        fun newInstance() = TodoFormFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_todo_form, container, false)
        viewDataBinding = FragmentTodoFormBinding.bind(root).apply {
            this.viewmodel = formViewModel
        }
        val toolbar: Toolbar = viewDataBinding.formAppbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize
        navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        viewDataBinding.formAppbar.setupWithNavController(navController, appBarConfiguration)

        //viewDataBinding.formAppbar.menu.getItem(0).icon.setTint(MaterialColors.getColor(view, com.google.android.material.R.attr.colorError))

        setupNavigation()
        formViewModel.start(args.todoId)

    }

    private fun setupNavigation() {
        formViewModel.taskUpdateEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TodoFormFragmentDirections
                .actionTodoFormFragmentToMainFragment()
            findNavController().navigate(action)
        })
    }
}