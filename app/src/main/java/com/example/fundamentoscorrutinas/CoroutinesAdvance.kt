package com.example.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import java.util.concurrent.TimeoutException

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 22/9/22 at 16:27
 * More info: zambranomoya74@gmail.com
 ****/

val countries = listOf("Granada", "Málaga", "Sevilla", "Jaen", "Cádiz", "Almería")

fun main() {
    // basicChannel()
    // closeChannel()
    // produceChannel()
    // pipeLines() // canal consume otro canal
    // bufferChannel()
    exceptions()
    readLine()
}

fun exceptions() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Notifica al programador... $throwable in $coroutineContext")
        println()

        if (throwable is java.lang.ArithmeticException)
            println("Mostrar mensaje de reintentar....")
    }
    runBlocking {
        newTopic("Manejo de excepciones")
        launch {
            try {
                delay(100)
                //throw Exception()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val globalScope = CoroutineScope(Job() + exceptionHandler)
        globalScope.launch {
            delay(200)
            throw TimeoutException()
        }

        CoroutineScope(Job() + exceptionHandler).launch {
            val result = async {
                delay(500)
                multiLambda(3, 5){
                    if (it > 5 ) throw java.lang.ArithmeticException()
                }
            }
            println("Result: ${result.await()}")
        }

        val channel = Channel<String>()
        CoroutineScope(Job()).launch(exceptionHandler) {
            delay(800)
            countries.forEach{
                channel.send(it)
                if (it.equals("Sevilla")) channel.close()
            }
        }
        channel.consumeEach { println(it) }
    }
}

fun bufferChannel() {
    runBlocking {
        newTopic("Buffer para channels")
        val time = System.currentTimeMillis()
        val channel = Channel<String>()
        launch {
            countries.forEach {
                delay(100)
                channel.send(it)
            }
            channel.close()
        }
        launch {
            delay(1_000)
            channel.consumeEach { println(it) }
            println("Time: ${System.currentTimeMillis() - time}ms")
        }

        val bufferTime = System.currentTimeMillis()
        val bufferChannel = Channel<String>(3)
        launch {
            countries.forEach {
                delay(100)
                bufferChannel.send(it)
            }
            bufferChannel.close()
        }
        launch {
            delay(1_000)
            bufferChannel.consumeEach { println(it) }
            println("B-> Time: ${System.currentTimeMillis() - bufferTime}ms")
        }
    }
}

fun CoroutineScope.produceFoods(cities: ReceiveChannel<String>): ReceiveChannel<String> = produce { 
    for (city in cities) {
        val food = getFoodByCity(city)
        send("$food desde $city")
    }
}

suspend fun getFoodByCity(city: String): String {
    delay(300)
    return when(city){
        "Granada" -> "Cabrito"
        "Málaga" -> "Pescaito"
        "Sevilla" -> "Ajo blanco"
        "Jaen" -> "Aceite"
        "Cádiz" -> "Tortilla de camarones"
        "Almería" -> "Pulpo"
        else -> "Sin datos"
    }
}

fun pipeLines() {
    runBlocking {
        newTopic("Pipelines")
        val citiesChannel = produceCities()
        val foodsChannel = produceFoods(citiesChannel)
        foodsChannel.consumeEach { println(it) }
        citiesChannel.cancel()
        foodsChannel.cancel()
        println("Todo finalizado")
    }
}



fun CoroutineScope.produceCities(): ReceiveChannel<String> = produce {
    countries.forEach{ send(it) }
}
fun produceChannel() {
    runBlocking {
        newTopic("Canales y el patrón productor-consumidor")
        val names = produceCities()
        names.consumeEach { println(it) }
    }
}

fun closeChannel() {
   runBlocking {
       newTopic("Cerrar canal")
       val channel = Channel<String>()
       launch {
           countries.forEach {
               channel.send(it)
               //if (it.equals("Málaga")) channel.close()
              /* if (it.equals("Málaga")) {
                   channel.close()
                   return@launch
               }*/
           }
           // channel.close()
       }
   /*    for (value in channel){
            println(value)
       }*/
      /* while (!channel.isClosedForReceive) {
           println(channel.receive())
       }*/

       channel.consumeEach { println(it) }
   }

}

// Forma de transmitir flojo de valores entre coroutines
fun basicChannel() {
    runBlocking {
        newTopic("Canal básico")
        val channel = Channel<String>()
        launch {
            countries.forEach{
                channel.send(it)
            }
        }

       /* repeat(countries.size) {
            println(channel.receive())
        }*/

        for (value in channel) {
            println(value)
        }
    }


}
