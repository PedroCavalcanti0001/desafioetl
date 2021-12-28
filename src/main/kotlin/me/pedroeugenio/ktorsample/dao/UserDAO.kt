package me.pedroeugenio.ktorsample.dao

import org.jetbrains.exposed.sql.Table

object UserDAO : Table("users") {
    val no = integer("no")
    val login = char("login", 8)
    val name = char("name", 36)
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(no) }
}