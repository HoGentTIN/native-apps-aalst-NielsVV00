package com.example.project3pt

import android.app.AlertDialog
import com.example.project3pt.databinding.FragmentHomeBinding
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.project3pt.R
import com.example.project3pt.fragments.home.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false
        )

        setHasOptionsMenu(true)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        binding.wedstrijd.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_wedstrijdLijstFragment))
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_option -> {
                openDialogLogout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDialogLogout() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(R.layout.dialog)
        val alert = dialogBuilder.create()
        alert.show()
        alert.findViewById<TextView>(R.id.dialog_info).text = "Weet u zeker dat u wilt uitloggen?"
        alert.findViewById<Button>(R.id.dialog_ok).setOnClickListener{
            homeViewModel.logout()
            findNavController().navigate(R.id.loginFragment)
            alert.cancel()
        }
        alert.findViewById<Button>(R.id.dialog_cancel).setOnClickListener{
            alert.cancel()
        }
    }
}
