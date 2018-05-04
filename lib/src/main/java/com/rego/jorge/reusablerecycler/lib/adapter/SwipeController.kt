package com.rego.jorge.reusablerecycler.lib.adapter

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.MotionEvent
import com.rego.jorge.reusablerecycler.lib.main.ReusableRecycler


/**
 * Created by jorge.rego.casas on 16/04/2018.
 */

class SwipeController<out S : Any>(private val buttonWidth: Float) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val adapter = recyclerView.adapter as ReusableAdapter<ReusableRecycler.ReusableItem<S>>
        return if (adapter.elementList[viewHolder.adapterPosition].swipeBack) {
            makeMovementFlags(0, LEFT)
        } else {
            makeMovementFlags(0, RIGHT)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as ReusableAdapter<S>.ReusableViewHolder).foregroundView
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val reusableViewHolder = (viewHolder as ReusableAdapter<S>.ReusableViewHolder)
        val adapter = recyclerView.adapter as ReusableAdapter<S>
        val foregroundView = reusableViewHolder.foregroundView
        var dX = dX

        if (adapter.elementList[reusableViewHolder.adapterPosition].swipeBack && isCurrentlyActive) {
            dX += buttonWidth
        }
        if (adapter.elementList[reusableViewHolder.adapterPosition].swipeBack && dX < buttonWidth && !isCurrentlyActive) {
            dX = buttonWidth
        }


        if (actionState == ACTION_STATE_SWIPE) {
            if (isCurrentlyActive) {
                if (adapter.elementList[reusableViewHolder.adapterPosition].state != ButtonsState.GONE) {
                    if (dX < buttonWidth / 3) {
                        adapter.elementList[reusableViewHolder.adapterPosition].state = ButtonsState.GONE
                        recyclerView.setOnTouchListener { _, event ->
                            if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
                                adapter.elementList[viewHolder.adapterPosition].swipeBack = false
                                recyclerView.setOnTouchListener { _, _ -> false }
                            }
                            false
                        }
                        reusableViewHolder.hideButtons()
                    }
                } else {
                    if (dX >= buttonWidth) {
                        adapter.elementList[reusableViewHolder.adapterPosition].state = ButtonsState.VISIBLE
                        recyclerView.setOnTouchListener { _, event ->
                            if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
                                adapter.elementList[viewHolder.adapterPosition].swipeBack = true
                                recyclerView.setOnTouchListener { _, _ -> false }
                            }
                            false
                        }
                        reusableViewHolder.showButtons()
                    }
                }
            }

            val swipeBack = adapter.elementList[reusableViewHolder.adapterPosition].swipeBack
            val state = adapter.elementList[reusableViewHolder.adapterPosition].state
            dX = when {
                state != ButtonsState.GONE -> when {
                    isCurrentlyActive && swipeBack -> dX
                    isCurrentlyActive && !swipeBack -> dX
                    !isCurrentlyActive && swipeBack -> dX
                    else -> dX
                }
                else -> when {
                    isCurrentlyActive && swipeBack -> Math.max(dX, 0F)
                    isCurrentlyActive && !swipeBack -> Math.max(dX, 0F)
                    !isCurrentlyActive && swipeBack -> dX
                    else -> 0F
                }
            }

            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
                    c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive
            )
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }


}