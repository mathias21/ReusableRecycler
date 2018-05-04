package com.rego.jorge.reusablerecycler.lib.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import com.rego.jorge.miahorrador.lib.adapter.BaseViewHolder

/**
 * Created by jorge.rego.casas on 27/07/2017.
 */

abstract class BaseAdapter<S : Any, T : BaseViewHolder<S>> : RecyclerView.Adapter<T>() {

    companion object {
        private val TAG = "BASEADAPTER"
    }

    var elementList = mutableListOf<S>()
        set(value) {
            Log.d(TAG, "setElementList: Adding elements to Adapter: " + value.size)
            this.elementList.clear()
            this.elementList.addAll(value)
            notifyDataSetChanged()
        }

    init {
        this.setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        checkForPosition(position)
        return elementList[position].hashCode().toLong()
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        checkForPosition(position)
        holder.bind(elementList[position])
    }

    override fun getItemCount(): Int {
        return elementList.size
    }

    private fun checkForPosition(position: Int) {
        if (position >= elementList.size) {
            throw IndexOutOfBoundsException(elementList.size.toString() + " elements in list")
        }
    }
}
