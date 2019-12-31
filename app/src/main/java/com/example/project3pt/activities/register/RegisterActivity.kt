package com.example.project3pt.activities.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.project3pt.activities.MainActivity
import com.example.project3pt.R
import com.example.project3pt.activities.login.LoginActivity
import com.example.project3pt.databinding.ActivityRegisterBinding
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegistreerViewModel by viewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)

        // Observer voor te navigeren naar home
        registerViewModel.registerSuccess.observe(this, androidx.lifecycle.Observer { result ->
            if(result){
                navigeerNaarHomeActivity()
            }
        })

        // Button onclick listeners
        binding.btnRegistreren.setOnClickListener{ registreren() }

        binding.btnAlEenAccount.setOnClickListener{ navigeerNaarLoginActivity()}

        // Overlay blokkeert clicks
        binding.llProgressBar.setOnClickListener{}
    }

    fun registreren() {
        if (!validate()) {
            onRegisterFailed()
            return
        }

        binding.btnRegistreren.setEnabled(false)

        // Progress bar aanzetten
        binding.llProgressBar.visibility = View.VISIBLE

        val emailText = binding.textEmail.text.toString()
        val passwordText = binding.textPassword.text.toString()
        val firstNameText = binding.textFirstname.text.toString()
        val lastNameText = binding.textLastname.text.toString()

        registerViewModel.register(emailText,passwordText, firstNameText, lastNameText)

        // Wacht 3 seconden, dan kijken naar antwoord
        android.os.Handler().postDelayed(
            {
                // On complete call either onLoginSuccess or onLoginFailed
                registerViewModel.registerSuccess.observe(this, androidx.lifecycle.Observer { result ->
                    if(result){
                        onRegisterSuccess()
                    } else{
                        onRegisterFailed()
                    }
                })
            }, 3000
        )
    }

    override fun onBackPressed() {
        // Pijltje terug bij registeren > naar login
        navigeerNaarLoginActivity()
    }

    fun onRegisterSuccess() {
        binding.btnRegistreren.setEnabled(true)
        binding.llProgressBar.visibility = View.GONE
    }

    private fun navigeerNaarHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun navigeerNaarLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun onRegisterFailed() {
        binding.llProgressBar.visibility = View.GONE
        Toast.makeText(baseContext, "Registreren mislukt.", Toast.LENGTH_LONG).show()
        binding.btnRegistreren.setEnabled(true)
    }

    fun validate(): Boolean {
        var valid = true

        val emailText = binding.textEmail.text.toString()
        val passwordText = binding.textPassword.text.toString()
        val passwordConfirmText = binding.textPasswordConfirm.text.toString()
        val firstNameText = binding.textFirstname.text.toString()
        val lastNameText = binding.textLastname.text.toString()

        if (passwordText != passwordConfirmText) {
            binding.textPasswordConfirm.setError("de wachtwoorden komen niet overeen")
            valid = true
        }
        if (passwordText.length < 6) {
            binding.textPassword.setError("Het wachtwoord is te kort")
            valid = true
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            binding.textEmail.setError("Gelieve een geldig e-mailadres te gebruiken")
            valid = true
        }
        if (emailText.isBlank() || passwordText.isBlank() || firstNameText.isBlank() || lastNameText.isBlank() || passwordConfirmText.isBlank()) {
            binding.textEmail.setError("Gelieve alle velden in te vullen")
            valid = true
        }


        return valid
    }

    companion object {
        private val TAG = "RegisterActivity"
        private val REQUEST_SIGNUP = 0
    }

    // Toetsenbord wegdoen bij wegklikken
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
