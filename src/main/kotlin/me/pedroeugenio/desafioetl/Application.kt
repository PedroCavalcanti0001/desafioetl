package me.pedroeugenio.desafioetl

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.pedroeugenio.desafioetl.plugins.configureHTTP
import me.pedroeugenio.desafioetl.routes.numbersRoute
import me.pedroeugenio.desafioetl.services.NumbersService

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Routing) {
            numbersRoute(NumbersService())
        }
        install(ContentNegotiation) {
            gson()
        }
        configureHTTP()
    }.start(wait = true)
}
