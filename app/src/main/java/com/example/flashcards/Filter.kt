package com.example.flashcards

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.MainActivity.Companion.PALETTECOLOR
import com.example.flashcards.MainActivity.Companion.PALETTESET
import com.example.flashcards.adapter.ColorAdapter
import com.example.flashcards.databinding.ActivityFilterBinding

class Filter : AppCompatActivity() {
    lateinit var binding: ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //PALETTECOLOR will be used to get a color from the recycler
        PALETTECOLOR = null

        //create a horizontal recycler of the colors
        setupFilterPalette()

        //filter icon is tapped and the selection is cleared
        binding.filterSelected.setOnClickListener { clearFilterSelection() }

        //filter button is tapped and the user is sent back to main activity with a filtered selection
        binding.filterButton.setOnClickListener { filterCardsets() }
    }

    //format and display the recycler view of colors
    private fun setupFilterPalette() {
        binding.filterPalette.setHasFixedSize(true)
        binding.filterPalette.layoutManager = (
                    LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                )
        binding.filterPalette.adapter = ColorAdapter(this, PALETTESET, binding.filterSelected, )
    }

    //clear the color selection, this will result in no filter being applied
    private fun clearFilterSelection() {
        binding.filterSelected.background = null
        PALETTECOLOR = null
    }

    //the user has chosen a color and the color is sent back to the Main Activity
    private fun filterCardsets() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if (PALETTECOLOR != null) {
            intent.putExtra("filter", PALETTECOLOR!!.color)
        }
        this.startActivity(intent)
        finish()
    }
}