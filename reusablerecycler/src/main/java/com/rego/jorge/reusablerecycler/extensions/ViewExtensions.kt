package com.rego.jorge.reusablerecycler.extensions

import android.view.View
import android.view.View.*

/**
 * Created by jorge.rego.casas on 05/04/2018.
 */
fun View.gone() {
    this.visibility = GONE
}

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}