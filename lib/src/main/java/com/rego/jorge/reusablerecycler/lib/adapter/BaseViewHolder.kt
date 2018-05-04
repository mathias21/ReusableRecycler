package com.rego.jorge.miahorrador.lib.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jorge.rego.casas on 27/07/2017.
 */

abstract class BaseViewHolder<in S : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(element: S)
}
