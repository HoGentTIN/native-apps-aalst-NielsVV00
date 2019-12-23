package com.example.project3pt


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.project3pt.R
import com.example.project3pt.databinding.FragmentRegistreerBinding
import com.example.project3pt.fragments.registreer.RegistreerViewModel
import com.example.project3pt.fragments.registreer.RegistreerViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_registreer.view.*
import kotlinx.android.synthetic.main.fragment_registreer.view.btn_registreren

/**
 * A simple [Fragment] subclass.
 */
class RegistreerFragment : Fragment() /*,AdapterView.OnItemSelectedListener*/ {

    private lateinit var registerViewModel: RegistreerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel = ViewModelProviders.of(this, RegistreerViewModelFactory()).get(RegistreerViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_registreer, container, false)

        registerViewModel.registerSuccess.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { result ->
                if (result) {
                    Toast.makeText(context, "De registratie was succesvol!", Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.action_registreerFragment_to_homeFragment)
                }
            })

        // Er wordt op registreer gedrukt:
        view.btn_registreren.setOnClickListener {
            val emailText = view.email_registreer.text.toString()
            val firstNameText = view.voornaam_registreer.text.toString()
            val lastNameText = view.achternaam_registreer.text.toString()
            val passwordText = view.wachtwoord_registreer.text.toString()


            var error = false

            if (passwordText.length < 6) {
                view.errorViewR.text = "Het wachtwoord is te kort"
                error = true
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                view.errorViewR.text = "Gelieve een geldig e-mailadres te gebruiken"
                error = true
            }
            if (emailText.isBlank() || passwordText.isBlank() || firstNameText.isBlank() || lastNameText.isBlank()) {
                view.errorViewR.text = "Gelieve alle velden in te vullen"
                error = true
            }

            // als alle velden ingevuld zijn & de twee wachtwoorden hetzelfde zijn, probeer de gebruiker te registreren
            if (!error) {
                registerViewModel.register(
                    emailText,
                    passwordText,
                    firstNameText,
                    lastNameText)

            }
        }
        return view
    }


    companion object {
        /**
         * Create a new instance of RegistreerFragment
         */
        fun newInstance(): RegistreerFragment {
            return RegistreerFragment()
        }
    }
}








