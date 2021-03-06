package com.example.buttongame

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerviewItemDecoration(private val bottomMargin: Int) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //if(parent.getChildAdapterPosition(view) != parent.adapter?.itemCount!! - 1)
        outRect.bottom = bottomMargin
    }
}