
package com.example.navitest.utils

import java.text.NumberFormat
import java.util.Locale

fun Int.toCLP(): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(this)
        .replace("$", "$ ")
}