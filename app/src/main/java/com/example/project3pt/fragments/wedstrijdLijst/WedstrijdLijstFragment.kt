package com.example.project3pt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.project3pt.databinding.FragmentWedstrijdLijstBinding
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdAdapter
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdLijstViewModel
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdListener
import org.koin.android.viewmodel.ext.android.viewModel

class WedstrijdLijstFragment : Fragment() {

    private lateinit var binding: FragmentWedstrijdLijstBinding
    private val vm: WedstrijdLijstViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_wedstrijd_lijst, container, false
        )

        binding.leeg.visibility = View.INVISIBLE
        vm.getWedstrijden()

        binding.refreshWedstrijdLijst.setOnRefreshListener {
            vm.resetWedstrijden()
            binding.loading.visibility = View.VISIBLE
            binding.leeg.visibility = View.INVISIBLE
            vm.getWedstrijden()
            binding.refreshWedstrijdLijst.isRefreshing = false
        }

        binding.floatingActionButton.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.action_wedstrijd_lijst_Fragment_to_maakWedstrijdFragment
        ))

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
                    WedstrijdLijstFragmentDirections.actionWedstrijdLijstFragmentToWedstrijdFragment(wedstrijd)
                )
                vm.onWedstrijdNavigated()
            }
        })

        vm.wedstrijden.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                binding.loading.visibility = View.INVISIBLE
            if(it.isNullOrEmpty()){
                binding.leeg.visibility = View.VISIBLE
            }
        })

        return binding.root
    }
}
