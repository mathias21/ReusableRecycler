package com.rego.jorge.reusablerecycler.adapter

import android.content.res.ColorStateList
import android.os.Handler
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.support.v4.widget.ImageViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.rego.jorge.reusablerecycler.R
import com.rego.jorge.reusablerecycler.extensions.invisible
import com.rego.jorge.reusablerecycler.extensions.visible
import com.rego.jorge.reusablerecycler.main.ReusableRecycler
import com.rego.jorge.reusablerecycler.main.StableIdElement
import kotlinx.android.synthetic.main.row_container_layout.view.*


/**
 * Created by jorge.rego.casas on 13/04/2018.
 */
class ReusableAdapter<S : StableIdElement>(
        @LayoutRes private val rowLayout: Int,
        val onBind: (itemView: View, S, position: Int) -> Unit,
        val onClick: ((S, itemView: View) -> Unit)? = null,
        private var onEdit: ((S, Int) -> Unit)? = null,
        private var onRemove: ((S, Int) -> Unit)? = null,
        @ColorInt private val iconsColor: Int?
) : BaseAdapter<StableIdElement, ReusableRecycler.ReusableItem<S>, ReusableAdapter<S>.ReusableViewHolder>() {

    companion object {
        private const val SIDE_MARGIN = 16F
    }

    var selectedRow = -1
    var areButtonsShown = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReusableViewHolder {
        with(LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)) {
            LayoutInflater.from(parent.context).inflate(R.layout.row_container_layout, parent, false).let {
                it.rowContainer.addView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                iconsColor?.let { color ->
                    ImageViewCompat.setImageTintList(it.editButton, ColorStateList.valueOf(color))
                    ImageViewCompat.setImageTintList(it.deleteButton, ColorStateList.valueOf(color))
                }
                return ReusableViewHolder(it)
            }
        }
    }

    override fun onBindViewHolder(holder: ReusableViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (!areButtonsShown) {
            if (position == 0) {
                val set = AnimationSet(true).also {
                    it.addAnimation(TranslateAnimation(0F, getButtonWidth(), 0F, 0F).apply {
                        duration = 300
                        startOffset = 1000
                        setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationRepeat(animation: Animation?) {}

                            override fun onAnimationStart(animation: Animation?) {}

                            override fun onAnimationEnd(animation: Animation?) {
                                holder.showButtons()
                                Handler().postDelayed({
                                    holder.hideButtons()
                                }, 500)
                                AnimationSet(true).also {
                                    it.addAnimation(TranslateAnimation(getButtonWidth(), 0F, 0F, 0F).apply {
                                        duration = 300
                                        startOffset = 400
                                    })
                                    holder.foregroundView.startAnimation(it)
                                }
                            }
                        })
                    })
                }
                holder.foregroundView.startAnimation(set)
                areButtonsShown = true
            }
        }
    }

    inner class ReusableViewHolder(itemView: View) : BaseViewHolder<ReusableRecycler.ReusableItem<S>>(itemView) {

        var foregroundView: View = itemView.rowContainer

        init {

            itemView.rowContainer?.setOnClickListener {
                if (selectedRow != adapterPosition) {
                    val oldSelectedPosition = selectedRow
                    selectedRow = adapterPosition
                    notifyItemChanged(adapterPosition)
                    notifyItemChanged(oldSelectedPosition)
                }

                onClick?.invoke(elementList[adapterPosition].item, itemView)
            }
            onEdit?.let { onEditFunction ->
                itemView.editButton?.apply {
                    setOnClickListener { onEditFunction.invoke(elementList[adapterPosition].item, adapterPosition) }
                }
            }
            onRemove?.let { onRemoveFunction ->
                itemView.deleteButton?.apply {
                    setOnClickListener {
                        onRemoveFunction.invoke(elementList[adapterPosition].item, adapterPosition)
                    }
                }
            }
        }

        override fun bind(element: ReusableRecycler.ReusableItem<S>) {
            if (element.state == ButtonsState.VISIBLE) {
                showButtons()
                foregroundView.x = getButtonWidth()
            } else {
                hideButtons()
                foregroundView.x = 0F
            }
            onBind(itemView, element.item, adapterPosition)
        }

        fun showButtons() {
            onEdit?.let {
                itemView.editButton?.visible()
            }
            onRemove?.let {
                itemView.deleteButton?.visible()
            }
        }

        fun hideButtons() {
            itemView.editButton?.invisible()
            itemView.deleteButton?.invisible()
        }
    }

    fun getButtonWidth() = (onEdit?.let { 80F } ?: 0F) + (onRemove?.let { 80F }
            ?: 0F) + 2 * SIDE_MARGIN

    fun isItemSelected() = selectedRow != -1
}

enum class ButtonsState {
    GONE,
    VISIBLE
}