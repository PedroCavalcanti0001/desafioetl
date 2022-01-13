package me.pedroeugenio.desafioetl.routes

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.pedroeugenio.desafioetl.services.NumbersService

fun Route.numbersRoute(numbersService: NumbersService) {
    route("/api") {
        get("/numeerosordenados") {
            call.respond(
                HttpStatusCode.NotAcceptable,
                Gson().toJson(numbersService.sortedNumbers())
            )
        }
    }
}
