package com.example.appgym

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Agrega espacio vertical entre items de un RecyclerView, sin tener
// que ponerle margenes al layout de cada fila.
class EspaciadoVertical(private val espacioDp: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val espacioPx = (espacioDp * view.context.resources.displayMetrics.density).toInt()
        outRect.bottom = espacioPx
    }
}