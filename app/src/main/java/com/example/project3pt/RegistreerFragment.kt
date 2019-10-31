package android.example.projectsharptake3


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.project3pt.R
import com.example.project3pt.databinding.FragmentRegistreerBinding

/**
 * A simple [Fragment] subclass.
 */
class RegistreerFragment : Fragment() /*,AdapterView.OnItemSelectedListener*/ {

    private lateinit var binding: FragmentRegistreerBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_registreer, container, false
        )

        binding.btnRegistreren.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registreerFragment_to_homeFragment))

        return binding.root


    }


}








