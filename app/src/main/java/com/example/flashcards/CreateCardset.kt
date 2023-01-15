package com.example.flashcards

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.MainActivity.Companion.PALETTECOLOR
import com.example.flashcards.MainActivity.Companion.PALETTESET
import com.example.flashcards.adapter.ColorAdapter
import com.example.flashcards.databinding.ActivityCreateBinding
import com.example.flashcards.model.Cardset
import com.example.flashcards.model.Flashcard
import com.example.flashcards.model.Palette
import com.google.android.material.snackbar.Snackbar

class CreateCardset : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //PALETTECOLOR will be used and initialized with a default color from the set
        PALETTECOLOR = Palette(PALETTESET[0].color)

        //make color selection a horizontal scrollable recycler view
        binding.createPalette.setHasFixedSize(true)
        binding.createPalette.layoutManager = (
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL, false
            )
        )
        binding.createPalette.adapter = ColorAdapter(this, PALETTESET, binding.createPaletteSelected)

        //create button is tapped and passes the new cardset back to main activity
        binding.buttonCreate.setOnClickListener {
            //tells user to input title if not filled
            if (binding.createTitle.text.isNullOrBlank()) {
                snackbarShow(it, this.resources.getString(R.string.create_error_empty_title))
                return@setOnClickListener
            }

            //sets variables needed to create new cardset
            var flashcards: List<Flashcard> = listOf<Flashcard>()
            val inputFront: List<String> = binding.createFront.text.toString().split("\n")
            val inputBack: List<String> = binding.createBack.text.toString().split("\n")

            //tells user if and what problem is associated with entry inputs
            if (inputFront.size + inputBack.size == 0) {
                snackbarShow(it, this.resources.getString(R.string.create_error_empty_entries))
                return@setOnClickListener
            }
            if (inputFront.size != inputBack.size) {
                snackbarShow(it, this.resources.getString(R.string.create_error_unbalanced_entries) +
                        " (currently ${inputFront.size} ${resources.getString(R.string.create_error_front)}" +
                        " and ${inputBack.size} ${resources.getString(R.string.create_error_back)})")
                return@setOnClickListener
            }

            //adds all specified strings from input to flashcard list
            for (i in 0..inputFront.lastIndex) {
                val flashcard = Flashcard(inputFront[i], inputBack[i])
                flashcards += flashcard
            }

            //make cardset object from user-specified title, color, and flashcard content
            val newCardset = Cardset(binding.createTitle.text.toString(), PALETTECOLOR!!.color, flashcards)

            //go back to Main Activity with new cardset data
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("newCardset", newCardset)
            this.startActivity(intent)
            finish()
        }
    }

    //snackbar message shortcut
    fun snackbarShow(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}