package com.example.project3pt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.project3pt.databinding.FragmentWedstrijdBinding
import com.example.project3pt.fragments.wedstrijd.DeelnemerAdapter
import com.example.project3pt.fragments.wedstrijd.WedstrijdViewModel
import com.example.project3pt.fragments.wedstrijd.WedstrijdViewModelFactory

class WedstrijdFragment : Fragment() {

    private lateinit var binding: FragmentWedstrijdBinding
    private lateinit var vm: WedstrijdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_wedstrijd, container, false
        )
        binding.empty.isVisible = false
        val arguments = WedstrijdFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = WedstrijdViewModelFactory(arguments.wedstrijdKey)

        vm = ViewModelProviders.of(this, viewModelFactory).get(WedstrijdViewModel::class.java)
        vm.wedstrijd.observe(this, Observer{
            it?.let{
                Log.i("wedstrijd", it.toString())
                binding.wedstrijdSoort.text = it.soort
                binding.wedstrijdLocatie.text = it.plaats
            }
        })


        val adapter = DeelnemerAdapter()
        binding.wedstrijdDeelnemers.adapter =adapter

        vm.deelnemers.observe(this, Observer {
            it?.let{
                binding.empty.isVisible = false
                adapter.data = it
                if(it.isEmpty()){
                    binding.empty.isVisible = true
                }
                binding.loading.visibility = View.INVISIBLE
            }
        })

        binding.neemDeel.setOnClickListener {
            vm.neemDeel()
        }

        return binding.root
    }
}