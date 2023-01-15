package com.example.flashcards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flashcards.databinding.ActivityCardViewerBinding
import com.example.flashcards.model.Cardset
import com.example.flashcards.model.Flashcard

class CardViewer : AppCompatActivity() {
    lateinit var binding: ActivityCardViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intialize intent extra information to be used
        val cardset: Cardset = intent.getSerializableExtra("flashcards") as Cardset
        var flashcards: List<Flashcard> = cardset.flashcards.shuffled()

        //variable that keeps track of which card of the cardset is being used
        var cardsetIndex: Int = 0

        //function that is ran in order to update the activity's views
        fun updateViewer() {
            binding.cardViewerFace.text = flashcards[cardsetIndex].current
            binding.cardViewerIndexIndicator.text = "${cardsetIndex+1}/${flashcards.size}"
            binding.cardViewerIndicator.setImageResource(when (flashcards[cardsetIndex].isFront) {
                true -> R.drawable.ic_card_viewer_front
                else -> R.drawable.ic_card_viewer_back
            })
        }

        //intiializes activity and colors background so it is ready for user
        binding.cardViewerBackground.setBackgroundColor(ContextCompat.getColor(this, cardset.color))
        title = cardset.title
        updateViewer()

        //flip button has been tapped and the card is flipped
        binding.cardViewerButtonFlip.setOnClickListener {
            flashcards[cardsetIndex].flip()
            updateViewer()
        }
        //right button has been tapped and the card next card is shown starting at the front
        binding.cardViewerButtonRight.setOnClickListener {
            if (++cardsetIndex > flashcards.lastIndex) {cardsetIndex = 0}
            flashcards[cardsetIndex].reset()
            updateViewer()
        }
        //left button has been tapped and the previous card is shown stating at the front
        binding.cardViewerButtonLeft.setOnClickListener {
            if (--cardsetIndex < 0) {cardsetIndex = flashcards.lastIndex}
            flashcards[cardsetIndex].reset()
            updateViewer()
        }
        //randomize button is tapped and cards are shuffled around
        binding.cardViewerButtonRandomize.setOnClickListener {
            flashcards = flashcards.shuffled()
            cardsetIndex = 0
            flashcards[cardsetIndex].reset()
            updateViewer()
        }
    }
}