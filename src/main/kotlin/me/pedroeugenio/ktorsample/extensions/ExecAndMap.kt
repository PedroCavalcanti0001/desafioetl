package me.pedroeugenio.ktorsample.extensions

import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.ResultSet

fun String.execAndMap(transform : (ResultSet) -> Any) : List<Any> {
    val result = arrayListOf<Any>()
    TransactionManager.current().exec(this) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}