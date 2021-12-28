package me.pedroeugenio.ktorsample

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.pedroeugenio.ktorsample.plugins.configureHTTP
import me.pedroeugenio.ktorsample.routes.userRout
import me.pedroeugenio.ktorsample.services.UserService

fun main() {
    Database.connect()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Routing) {
            userRout(UserService())
        }
        install(ContentNegotiation) {
            gson()
        }
        //configureSecurity()
        configureHTTP()
    }.start(wait = true)
}
