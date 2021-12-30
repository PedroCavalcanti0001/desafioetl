package me.pedroeugenio.ktorsample.extensions

fun String.isKeyword(): Boolean {
    val keywordList = arrayListOf(
        "as", "break", "class", "continue", "do", "else",
        "false", "for", "fun", "if", "in", "interface",
        "is", "null", "object", "package", "return", "super",
        "this", "throw", "true", "try", "typealias", "typeof",
        "val", "var", "when", "while"
    )
    return keywordList.contains(this.lowercase())
}