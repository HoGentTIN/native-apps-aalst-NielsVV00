package com.example.project3pt.fragments.maakWedstrijd

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project3pt.R
import com.example.project3pt.databinding.FragmentMaakWedstrijdBinding
import java.util.Calendar


class MaakWedstrijdFragment: Fragment(){

    lateinit var binding: FragmentMaakWedstrijdBinding
    lateinit var vm: MaakWedstrijdViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maak_wedstrijd, container, false)
        vm = ViewModelProviders.of(this).get(MaakWedstrijdViewModel::class.java)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.kiesDatum.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ view, jaar, maand, dag ->
                binding.dateField.setText(vm.saveDate(jaar, maand, dag))
            }, year, month, day)
            dpd.show()
        }

        binding.voegWedToe.setOnClickListener {
            val soort = binding.editSoort.text.toString()
            val locatie = binding.editLocatie.text.toString()
            if(soort.isBlank() || locatie.isBlank()) {
                Toast.makeText(requireContext(),"Gelieve alle velden in te vullen.",Toast.LENGTH_LONG)
            }
            else if(vm.datum.before(Calendar.getInstance().time)){
                Toast.makeText(requireContext(), "Gelieve een datum in de toekomst te kiezen", Toast.LENGTH_LONG)
            }else{
                vm.postWedstrijd(soort, locatie)
            }

            vm.postSucces.observe(this, Observer{
                findNavController().navigateUp()
            })

        }

        return binding.root
    }
}