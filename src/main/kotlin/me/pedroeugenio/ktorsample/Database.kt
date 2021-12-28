package me.pedroeugenio.ktorsample

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object Database {
    var url = "jdbc:mysql://10.1.0.242:3306/sqldados"
    var user = "root"
    var password = "7ufp206mrpjy2021"
    val driver = "com.mysql.jdbc.Driver"

    private fun hikari(production: Boolean = false): HikariDataSource {
        val config = HikariConfig()
        /*
        if (production) {
            url = "jdbc:mysql://10.0.0.251:3306/sqldados"
            user = "api-auditoria"
        }r
         */
        config.driverClassName = driver
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.maximumPoolSize = 5
        config.isAllowPoolSuspension = true
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }

    fun connect() {
        //Database.connect(url,user = user,password = password,driver = driver,)
        Database.connect(hikari())
    }

}