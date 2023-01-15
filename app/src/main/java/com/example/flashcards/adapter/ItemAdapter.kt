package com.example.flashcards.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.CardViewer
import com.example.flashcards.MainActivity.Companion.DATASET
import com.example.flashcards.R
import com.example.flashcards.model.Cardset
import com.google.android.material.snackbar.Snackbar
import java.io.ObjectOutputStream

class ItemAdapter(
    private val context: Context,
    private var dataset: List<Cardset>,
    private val recycler: RecyclerView
):  RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    //gets relevant views needed for laying out each card
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val colorView: ImageView = view.findViewById(R.id.item_color)
        val cardCount: TextView = view.findViewById(R.id.item_count)
        val itemLayout: ConstraintLayout = view.findViewById(R.id.item_cardset)
    }

    //returns the holder as objects in the item_title layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    //dynamically assign text and color depending on cardset data
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.textView.text = item.title
        holder.cardCount.text = "${item.flashcards.size} ${if (item.flashcards.size==1) "Card" else "Cards"}"
        holder.colorView.setBackgroundColor(ContextCompat.getColor(context, item.color))


        //item in recycler is tapped and user is taken to respective card viewer activity
        holder.itemLayout.setOnClickListener {
            val intent = Intent(holder.itemLayout.context, CardViewer::class.java)
            intent.putExtra("flashcards", item)
            holder.itemLayout.context.startActivity(intent)
        }
        //item is long tapped and the recycler and DATASET is updated with the cardset removed
        holder.itemLayout.setOnLongClickListener {
            //
            Snackbar.make(it, "Delete \"${dataset[position].title}\"?", Snackbar.LENGTH_LONG)
                .setAction("DELETE") {
                    DATASET = DATASET.filter { it != item }
                    dataset = dataset.filter { it != item }
                    recycler.adapter = ItemAdapter(context, dataset, recycler)
                    context.openFileOutput("cardsets.save", Context.MODE_PRIVATE).use {
                        ObjectOutputStream(it).writeObject(DATASET)
                    }
                }
                .show()
            true
        }
    }

    //size of item list
    override fun getItemCount(): Int {
        return dataset.size
    }

}
