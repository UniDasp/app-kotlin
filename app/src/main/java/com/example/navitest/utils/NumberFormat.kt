
package com.example.navitest.utils

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil

fun Int.toCLP(): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(this)
        .replace("$", "$ ")
}

fun Double.toCLP(): String {
    val rounded = ceil(this).toInt()
    return NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(rounded)
        .replace("$", "$ ")
}