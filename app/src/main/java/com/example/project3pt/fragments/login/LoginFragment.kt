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
import com.example.project3pt.databinding.FragmentLoginBinding
import com.example.project3pt.fragments.login.LoginViewModel
import com.example.project3pt.fragments.login.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() /*,AdapterView.OnItemSelectedListener*/ {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory()).get(LoginViewModel::class.java)

        // Als de gebruiker reeds ingelogd is bij start van de app, navigeer naar home
        loginViewModel.checkIsUserLoggedIn()

        loginViewModel.loginSucces.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result ->
            if(result){
                Toast.makeText(context, "Ingelogd!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })

        // Load the fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //enkel voor development
        view.email_login.setText("niels.vv123@gmail.com")
        view.wachtwoord_login.setText("azerty")

        // Definieer de onClickListener van de knop 'Aanmelden'
        view.btn_aanmelden.setOnClickListener{
            var email = view.email_login.text.toString()
            val passwordText = view.wachtwoord_login.text.toString()
            view?.clearFocus()

            var error = false

            if(email.isBlank()) {
                view.errorView.text = "Username can't be blank"
                error = true
            }
            if(passwordText.isBlank()){
                view.errorView.text = "Password can't be blank"
                error = true
            }

            // Indien beide velden ingevuld zijn, probeer dan in te loggen
            if (!error){
                loginViewModel.login(email,passwordText)
            }

        }

        // Navigeer naar register fragment
        view.btn_registreren.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registreerFragment))
        return view

    }
}








