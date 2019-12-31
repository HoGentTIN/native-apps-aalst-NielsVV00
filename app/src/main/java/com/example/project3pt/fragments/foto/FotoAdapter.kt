package com.example.project3pt.fragments.foto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project3pt.databinding.FotoItemBinding
import com.example.project3pt.models.Foto
import java.util.*

/*
deze klasse wordt gebruikt voor het dynamisch laden van alle wedstrijden in de wedstrijdenlijst
 */
class FotoAdapter(val clickListener: FotoListener) : ListAdapter<Foto, FotoAdapter.FotoViewHolder>(
    FotoDiffCallBack()
) {

    override fun onBindViewHolder(holder: FotoViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoViewHolder {
        return FotoViewHolder.from(
            parent
        )
    }
    /* dit is viewholder van een fiets => de code achter wat je ziet in de fietsenlijst voor elke fiets */
    class FotoViewHolder private constructor(val binding: FotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Foto,
            clickListener: FotoListener
        ) {
            binding.fotoItem.setImageBitmap(item.getImage())
            binding.foto = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FotoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FotoItemBinding.inflate(layoutInflater, parent, false)
                return FotoViewHolder(
                    binding
                )
            }
        }
    }
}
/* wordt gebruikt om te gaan kijken of er verschillen zijn opgetreden en gaat dan opnieuw laden bij verschillen */
class FotoDiffCallBack : DiffUtil.ItemCallback<Foto>() {
    override fun areItemsTheSame(oldItem: Foto, newItem: Foto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Foto, newItem: Foto): Boolean {
        return oldItem.fotoData == newItem.fotoData
    }
}

/* implementeert de onclicklistener voor elk fietsobject */
class FotoListener(val clickListener: (foto: Foto) -> Unit) {
    fun onClick(foto: Foto) = clickListener(foto)
}
