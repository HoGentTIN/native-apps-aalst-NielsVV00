package com.example.project3pt.fragments.wedstrijdLijst

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project3pt.databinding.WedstrijdLijstItemBinding
import com.example.project3pt.models.Wedstrijd

/*
deze klasse wordt gebruikt voor het dynamisch laden van alle wedstrijden in de wedstrijdenlijst
 */
class WedstrijdAdapter(val clickListener: WedstrijdListener): ListAdapter<Wedstrijd, WedstrijdAdapter.WedstrijdViewHolder>(
    WedstrijdDiffCallBack()
) {

    override fun onBindViewHolder(holder: WedstrijdViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WedstrijdViewHolder {
        return WedstrijdViewHolder.from(
            parent
        )
    }
    /* dit is viewholder van een fiets => de code achter wat je ziet in de fietsenlijst voor elke fiets */
    class WedstrijdViewHolder private constructor(val binding: WedstrijdLijstItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Wedstrijd,
            clickListener: WedstrijdListener
        ) {
            binding.wedstrijd = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WedstrijdViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WedstrijdLijstItemBinding.inflate(layoutInflater, parent, false)
                return WedstrijdViewHolder(
                    binding
                )
            }
        }
    }
}
/* wordt gebruikt om te gaan kijken of er verschillen zijn opgetreden en gaat dan opnieuw laden bij verschillen */
class WedstrijdDiffCallBack : DiffUtil.ItemCallback<Wedstrijd>(){
    override fun areItemsTheSame(oldItem: Wedstrijd, newItem: Wedstrijd): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Wedstrijd, newItem: Wedstrijd): Boolean {
        return oldItem == newItem
    }

}

/* implementeert de onclicklistener voor elk fietsobject */
class WedstrijdListener(val clickListener: (wedstrijdId: Long) -> Unit){
    fun onClick(wedstrijd: Wedstrijd) = clickListener(wedstrijd.id)
}