package me.pedroeugenio.ktorsample.routes

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.pedroeugenio.ktorsample.models.ErrorMessage
import me.pedroeugenio.ktorsample.services.UserService

fun Route.userRout(userService: UserService) {
    route("/user") {
        get("/hello") {
            call.respondText("Hello, world!")
        }
        get("/teste") {
            val users = userService.getAll()
            println(users)
            call.respond(HttpStatusCode.OK, Gson().toJson(users))
        }

        get("teste2/{login}") {
            val login = call.parameters["login"]
            val regex = "^[0-9]*[1-9][0-9]*\$".toRegex()
            if (login != null && regex.matches(login)) {
                val user = userService.findByNo(login.toInt())
                call.respond(HttpStatusCode.OK, Gson().toJson(user))
            } else {
                call.respond(HttpStatusCode.NotAcceptable, Gson().toJson(ErrorMessage(message = "parametro {login} não encontrado ou inválido!")))
            }
        }
    }
}
