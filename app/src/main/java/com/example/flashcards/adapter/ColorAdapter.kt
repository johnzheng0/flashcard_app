package com.example.flashcards.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.MainActivity.Companion.PALETTECOLOR
import com.example.flashcards.R
import com.example.flashcards.model.Palette

class ColorAdapter (
    private val context: Context,
    private val dataset: List<Palette>,
    private val paletteMain: ImageView
):  RecyclerView.Adapter<ColorAdapter.ItemViewHolder>() {

    //defines views in set
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val paletteSelector: ImageView = view.findViewById(R.id.create_palette_selected)
    }

    //something idk
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_color, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    //define how views should change
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.paletteSelector.setBackgroundColor(ContextCompat.getColor(context, item.color))
        holder.paletteSelector.contentDescription = item.color.toString()
        holder.paletteSelector.setOnClickListener {
            paletteMain.background = it.background
            PALETTECOLOR = Palette(item.color)
            paletteMain.setBackgroundColor(ContextCompat.getColor(context, PALETTECOLOR!!.color))
        }
    }

    //size of item list
    override fun getItemCount(): Int {
        return dataset.size
    }

}