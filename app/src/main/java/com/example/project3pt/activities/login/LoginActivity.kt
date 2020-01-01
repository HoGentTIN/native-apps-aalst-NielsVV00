package com.example.project3pt.activities.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import android.view.inputmethod.InputMethodManager
import android.view.MotionEvent
import android.content.Context
import com.example.project3pt.R
import com.example.project3pt.activities.MainActivity
import com.example.project3pt.activities.register.RegisterActivity
import com.example.project3pt.databinding.ActivityLoginBinding
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.llProgressBar.visibility = View.VISIBLE

        title = "Login"

        // Als de gebruiker reeds ingelogd is bij het starten van de app, navigeer naar home activity
        loginViewModel.checkIsUserLoggedIn()

        loginViewModel.loginSucces.observe(this, androidx.lifecycle.Observer { result ->
            Log.i("bool", result.toString())
            if(result){
                navigeerNaarHomeActivity()
            }
            else{
                binding.llProgressBar.visibility = View.GONE
            }
        })

        //enkel voor development
        //binding.emailTextField.setText("grietderoo@ocdebeweging.be")
        //binding.passwordTextField.setText("Griet123!")

        // Button onclick listeners
        binding.btnAanmelden.setOnClickListener(View.OnClickListener { login() })

        binding.btnRegistreren.setOnClickListener(View.OnClickListener {
            // Start the Register activity
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivityForResult(intent,
                REQUEST_SIGNUP
            )
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        // Overlay blokkeert clicks
        binding.llProgressBar.setOnClickListener(View.OnClickListener { })

    }

    fun login() {
        if (!validate()) {
            onLoginFailed()
            return
        }

        binding.btnAanmelden.setEnabled(false)

        // Progress bar aanzetten
        binding.llProgressBar.visibility = View.VISIBLE

        val email = binding.emailTextField.getText().toString()
        val password = binding.passwordTextField.getText().toString()

        loginViewModel.login(email, password)

        // Wacht 3 seconden, dan kijken naar antwoord
        android.os.Handler().postDelayed(
        {
            // On complete call either onLoginSuccess or onLoginFailed
            loginViewModel.loginSucces.observe(this, androidx.lifecycle.Observer { result ->
                if(result){
                    onLoginSuccess()
                } else{
                    onLoginFailed()
                }
            })
        }, 3000)

    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    fun onLoginSuccess() {
        binding.btnAanmelden.setEnabled(true)
        binding.llProgressBar.visibility = View.GONE
    }

    private fun navigeerNaarHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun onLoginFailed() {
        binding.llProgressBar.visibility = View.GONE
        Toast.makeText(baseContext, "Aanmelden mislukt.", Toast.LENGTH_LONG).show()
        binding.btnAanmelden.setEnabled(true)
    }

    fun validate(): Boolean {
        var valid = true

        val email = binding.emailTextField.getText().toString()
        val password = binding.passwordTextField.getText().toString()

        if (email.isEmpty()) {
            binding.emailTextField.setError("Vul een email adres in a.u.b.")
            valid = false
        } else {
            binding.emailTextField.setError(null)
        }

        if (password.isEmpty()) {
            binding.passwordTextField.setError("Vul een wachtwoord in a.u.b.")
            valid = false
        } else {
            binding.passwordTextField.setError(null)
        }

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
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
