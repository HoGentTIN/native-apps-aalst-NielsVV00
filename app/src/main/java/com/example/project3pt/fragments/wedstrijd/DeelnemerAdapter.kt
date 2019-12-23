package com.example.project3pt.fragments.wedstrijd

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project3pt.R
import com.example.project3pt.models.Deelnemer

class DeelnemerAdapter: RecyclerView.Adapter<TextItemViewHolder>(){
    var data = listOf<Deelnemer>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.voornaam + " " + item.achternaam
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.deelnemer_view_item, parent, false) as TextView
        return TextItemViewHolder(view)
    }

}

class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)
