package com.example.project3pt

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project3pt.activities.login.LoginActivity
import com.example.project3pt.databinding.FragmentHomeBinding
import com.example.project3pt.fragments.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false
        )

        setHasOptionsMenu(true)

        homeViewModel.init()

        homeViewModel.foto.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val imageBytes = Base64.getDecoder().decode(it.fotoData)
                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.foto.setImageBitmap(image)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_option -> {
                openDialogLogout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDialogLogout() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(R.layout.dialog)
        val alert = dialogBuilder.create()
        alert.show()
        alert.findViewById<TextView>(R.id.dialog_info).text = "Weet u zeker dat u wilt uitloggen?"
        alert.findViewById<Button>(R.id.dialog_ok).setOnClickListener {
            homeViewModel.logout()
            // Intent om te kunnen navigeren van activities
            val intent = Intent(this.activity, LoginActivity::class.java)
            startActivity(intent)
            this.activity!!.finish()
            this.activity!!.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            alert.cancel()
        }
        alert.findViewById<Button>(R.id.dialog_cancel).setOnClickListener {
            alert.cancel()
        }
    }
}
