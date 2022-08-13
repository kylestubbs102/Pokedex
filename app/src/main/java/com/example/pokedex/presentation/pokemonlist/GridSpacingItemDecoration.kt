package com.example.pokedex.presentation.pokemonlist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val verticalSpacing: Int,
    private val horizontalSpacing: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        if (column == 0) {
            outRect.right = horizontalSpacing
        } else {
            outRect.left = horizontalSpacing
        }
        outRect.top = verticalSpacing
        outRect.bottom = verticalSpacing
    }
}