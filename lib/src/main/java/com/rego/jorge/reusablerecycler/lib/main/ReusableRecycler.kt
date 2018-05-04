package com.rego.jorge.reusablerecycler.lib.main

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.rego.jorge.reusablerecycler.lib.R
import com.rego.jorge.reusablerecycler.lib.adapter.ButtonsState
import com.rego.jorge.reusablerecycler.lib.adapter.ReusableAdapter
import com.rego.jorge.reusablerecycler.lib.adapter.SwipeController
import com.rego.jorge.reusablerecycler.lib.extensions.visible
import kotlinx.android.synthetic.main.reusable_recycler_frame.view.*


/**
 * Created by jorge.rego.casas on 13/04/2018.
 */
class ReusableRecycler<in S : Any>(
        context: Context,
        frameLayout: FrameLayout,
        @LayoutRes private var rowLayoutResource: Int,
        onBind: (itemView: View, S, position: Int) -> Unit,
        onClick: ((S, itemView: View) -> Unit)? = null,
        private var onAdd: (() -> Unit)? = null,
        private var onEdit: ((S, Int) -> Unit)? = null,
        private var onRemove: ((S, Int) -> Unit)? = null,
        @ColorInt iconsColor: Int? = null
) : CoordinatorLayout(context) {

    private val reusableAdapter = ReusableAdapter(rowLayoutResource, onBind, onClick, onEdit, onRemove, iconsColor)

    init {
        with(LayoutInflater.from(context).inflate(R.layout.reusable_recycler_frame, this, true)) {
            reusableRecycler?.let {
                var buttonWidth = reusableAdapter.getButtonWidth()
                it.layoutManager = LinearLayoutManager(context)
                if (onEdit != null || onRemove != null) {
                    reusableAdapter.areButtonsShown = false
                    SwipeController<S>(buttonWidth).apply {
                        ItemTouchHelper(this).attachToRecyclerView(it)
                    }
                }
            }
            onAdd?.let {
                addElementFab?.visible()
                addElementFab?.setOnClickListener { onAdd?.invoke() }
            }
        }

        frameLayout.addView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun setElements(elementList: List<S>) {
        reusableRecycler?.apply {
            reusableAdapter.elementList = elementList.map {
                ReusableItem(it)
            }.toMutableList()
            adapter = reusableAdapter
        }
    }

    fun removeElementAtPosition(position: Int) {
        (reusableRecycler?.adapter as? ReusableAdapter<S>)?.let {
            it.elementList.removeAt(position)
            it.notifyItemRemoved(position)
        }
    }

    fun addItem(item: S) {
        (reusableRecycler?.adapter as? ReusableAdapter<S>)?.let {
            it.elementList.add(ReusableItem(item))
            it.notifyItemInserted(it.elementList.lastIndex)
        }
    }

    fun updateElementAtPosition(position: Int, element: S) {
        (reusableRecycler?.adapter as? ReusableAdapter<S>)?.let {
            it.elementList[position] = ReusableItem(element)
            it.notifyItemChanged(position)
        }
    }

    fun getSelectedElementPosition() = reusableAdapter.selectedRow

    data class ReusableItem<out S>(val item: S, var state: ButtonsState = ButtonsState.GONE, var swipeBack: Boolean = false)

}
