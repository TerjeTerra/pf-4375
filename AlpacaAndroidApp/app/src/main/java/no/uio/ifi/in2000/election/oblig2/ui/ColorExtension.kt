package no.uio.ifi.in2000.election.oblig2.ui

import android.util.Log
import androidx.compose.ui.graphics.Color

// parse a String to a color of a type that is recognized by Material3-design
// by method suggested by ChatGPT UiO
fun String?.toColor(): Color {
    Log.d("toColor", "inside toColor-function with code $this")
    return if(this.isNullOrEmpty()) {
        Color.Transparent //default color
    } else {
        try {
            Color(android.graphics.Color.parseColor(this))
        } catch (e: IllegalArgumentException) {
            Log.d("toColor", "error Illegal argument with code $this")
            Color.Transparent // fallback color in case the parsing fails
        }
    }
}
