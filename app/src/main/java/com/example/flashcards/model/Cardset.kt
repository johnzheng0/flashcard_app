package com.example.flashcards.model

data class Cardset(
    val title: String,
    val color: Int,
    val flashcards: List<Flashcard>
): java.io.Serializable {
    fun equals(other: Cardset): Boolean {
        if (this.title != other.title) {return false}
        if (this.color != other.color) {return false}
        return true
    }
}