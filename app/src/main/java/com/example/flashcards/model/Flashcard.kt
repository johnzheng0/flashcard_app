package com.example.flashcards.model

class Flashcard(
    private val front: String,
    private val back: String
    ): java.io.Serializable {
    public var current: String = front
    public var isFront: Boolean = true

    public fun flip(): String {
        current = when(current) {
            front -> back
            else -> front
        }
        isFront = !isFront
        return current
    }

    public fun reset() {
        current = front
        isFront = true
    }
}