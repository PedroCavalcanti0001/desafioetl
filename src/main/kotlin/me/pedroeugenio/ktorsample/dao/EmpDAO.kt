package me.pedroeugenio.ktorsample.dao

import org.jetbrains.exposed.sql.Table

object EmpDAO : Table("emp") {
    val no = integer("no")
    val storeno = integer("storeno")
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(no) }
}