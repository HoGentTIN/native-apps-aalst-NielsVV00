package com.example.project3pt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdAdapter
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdListener
import com.example.project3pt.databinding.FragmentWedstrijdLijstBinding
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdLijstViewModel


class WedstrijdLijstFragment : Fragment() {

    private lateinit var binding: FragmentWedstrijdLijstBinding
    private lateinit var vm: WedstrijdLijstViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        vm = ViewModelProviders.of(this).get(WedstrijdLijstViewModel::class.java)
        vm.getWedstrijden()

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_wedstrijd_lijst, container, false
        )

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
            it?.let{
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}