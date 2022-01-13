package me.pedroeugenio.desafioetl.services

import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NumbersService {
    private fun sort(array: ArrayList<Double>): ArrayList<Double> {
        var n = 0.0
        for (i in 0..array.size) {
            for (i2 in i + 1 until array.size) {
                if (array[i] > array[i2]) {
                    n = array[i]
                    array[i] = array[i2]
                    array[i2] = n
                }
            }
        }
        return array
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sortedNumbers(): ArrayList<Double> {
        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = GsonSerializer() {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
        val numbers = arrayListOf<Double>()
        return withContext(Dispatchers.IO) {
            var page = 1
            while (true) {
                try {
                    val response =
                        client.get<HttpStatement>("http://challenge.dienekes.com.br/api/numbers?page=$page") {
                            contentType(ContentType.Application.Json)
                        }.execute()

                    val numbersFromPage =
                        JsonParser.parseString(response.readText()).asJsonObject.getAsJsonArray("numbers")
                            .map { it.asDouble }

                    if (numbersFromPage.isEmpty()) {
                        println("chegamos ao fim")
                        break
                    }
                    numbers.addAll(numbersFromPage)
                } catch (ex: Exception) {
                    println("Não foi possivel buscar os numeros da pagina $page -> ${ex.message}")
                }
                page += 1
            }
            return@withContext sort(numbers)
        }
    }
}