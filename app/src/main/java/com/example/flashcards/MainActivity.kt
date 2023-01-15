package com.example.flashcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.adapter.ItemAdapter
import com.example.flashcards.databinding.ActivityMainBinding
import com.example.flashcards.model.Cardset
import com.example.flashcards.model.Flashcard
import com.example.flashcards.model.Palette
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //default selection of cardsets
    companion object  {
        //list of cardsets
        var DATASET = listOf<Cardset>(
                Cardset("Example", R.color.red, listOf<Flashcard>(
                        Flashcard("Front", "Back"),
                        Flashcard("7 - 4", "3"),
                        Flashcard("32 * 2", "64"),
                        Flashcard("5 + 3 * 2", "11"),
                    )
                )
            )

        //hardcoded colors that are available to cardsets
        val PALETTESET = listOf<Palette> (
            Palette(R.color.red),
            Palette(R.color.green),
            Palette(R.color.blue),
            Palette(R.color.yellow),
            Palette(R.color.orange),
            Palette(R.color.magenta),
        )

        var PALETTECOLOR: Palette? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intents are checked and dataset is given a list
        var shownList = updateList()

        //recycler view is filled with cardsets and oriented vertically
        setupCardsetList(binding.recyclerView, shownList)
    }

    private fun setupCardsetList(recycler: RecyclerView, list: List<Cardset>) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = (
                LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL, false
                )
                )
        recycler.adapter = ItemAdapter(this, list, recycler)
    }

    private fun updateList(): List<Cardset> {
        if (intent.hasExtra("newCardset")) {
            //DATASET gets a new cardset from intent extra
            val intentCardset: Cardset = intent.getSerializableExtra("newCardset") as Cardset
            DATASET += intentCardset
            //write DATASET to disk since a new cardset is added
            openFileOutput("cardsets.save", Context.MODE_PRIVATE).use {
                ObjectOutputStream(it).writeObject(DATASET)
            }
        } else if (intent.hasExtra("filter")) {
            //filters the cardset list of an intent specified color
            val intentFilter: Int = intent.getIntExtra("filter", 0) as Int
            return DATASET.filter { it.color == intentFilter }
        } else {
            //read saved data from disk if it exist
            if (File(filesDir, "cardsets.save").exists()) {
                openFileInput("cardsets.save").use {
                    DATASET = ObjectInputStream(it).readObject() as List<Cardset>
                }
            }
        }
        return DATASET //DATASET is unmodified
    }

    //adds plus button to topbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)

        val createButton = menu?.findItem(R.id.action_create)
        createButton?.icon = ContextCompat.getDrawable(this, R.drawable.ic_create)

        return true
    }

    //plus button is tapped and starts the cardset creation activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create -> {
                val intent = Intent(this, CreateCardset::class.java)
                this.startActivity(intent)
                return true
            }
            R.id.action_filter -> {
                val intent = Intent(this, Filter::class.java)
                this.startActivity(intent)
                return true
            }
        }
        return true
    }
}

