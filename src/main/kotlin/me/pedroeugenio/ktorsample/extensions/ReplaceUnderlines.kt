package me.pedroeugenio.ktorsample.extensions

import java.util.*

fun String.replaceUnderlines():String {
    var secondPart = this.indexOf('_').let { if (it == -1) null else this.substring(it + 1) }
    if(secondPart != null){
        var firstPart = this.replace(secondPart, "")
        firstPart = firstPart.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }.replace("_","")
        secondPart = secondPart.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        return "${firstPart}${secondPart}".replaceUnderlines()
    }
    return this
}