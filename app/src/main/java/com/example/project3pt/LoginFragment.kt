package android.example.projectsharptake3


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.project3pt.R
import com.example.project3pt.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() /*,AdapterView.OnItemSelectedListener*/ {

    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login, container, false
        )

        binding.btnAanmelden.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_homeFragment))
        binding.btnRegistreren.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registreerFragment))

        return binding.root


    }


}








