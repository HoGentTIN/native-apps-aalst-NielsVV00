package com.example.project3pt

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project3pt.activities.login.LoginActivity
import com.example.project3pt.databinding.FragmentWedstrijdLijstBinding
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdAdapter
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdLijstViewModel
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdListener
import org.koin.android.viewmodel.ext.android.viewModel

class MijnWedstrijdLijstFragment : Fragment() {

    private lateinit var binding: FragmentWedstrijdLijstBinding
    private val vm: WedstrijdLijstViewModel by viewModel()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_wedstrijd_lijst, container, false
        )

        vm.isMijnWedstrijden()
        vm.getWedstrijden()

        binding.refreshWedstrijdLijst.setOnRefreshListener {
            vm.resetWedstrijden()
            binding.loading.visibility = View.VISIBLE
            vm.getWedstrijden()
            binding.refreshWedstrijdLijst.isRefreshing = false
        }

        binding.floatingActionButton.visibility = View.INVISIBLE

        binding.leeg.text = "Je neemt nog niet deel aan één van de wedstrijden"
        binding.leeg.visibility = View.INVISIBLE

        val adapter =
            WedstrijdAdapter(
                WedstrijdListener { wedstrijdId ->
                    vm.onWedstrijdClicked(wedstrijdId)
                })
        binding.wedstrijdenLijst.adapter = adapter

        /* onclicklistener wanneer op de wedstrijd geklikt wordt */
        vm.navigateToWedstrijd.observe(this, Observer { wedstrijd ->
            wedstrijd?.let {
                this.findNavController().navigate(
                    MijnWedstrijdLijstFragmentDirections.actionMijnWedstrijdLijstFragmentToWedstrijdFragment(wedstrijd)
                )
                vm.onWedstrijdNavigated()
            }
        })

        vm.hasWedstrijden.observe(viewLifecycleOwner, Observer {
            Log.i("tf?", it.toString())
            if (it) {
                binding.leeg.visibility = View.INVISIBLE
            } else {
                binding.leeg.visibility = View.VISIBLE
            }
        })

        vm.wedstrijden.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                binding.loading.visibility = View.INVISIBLE
        })

        setHasOptionsMenu(true)

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
        alert.findViewById<Button>(R.id.dialog_ok).setOnClickListener {
            vm.logout()
            // Intent om te kunnen navigeren van activities
            val intent = Intent(this.activity, LoginActivity::class.java)
            startActivity(intent)
            this.activity!!.finish()
            this.activity!!.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            alert.cancel()
        }
        alert.findViewById<Button>(R.id.dialog_cancel).setOnClickListener {
            alert.cancel()
        }
    }
}
