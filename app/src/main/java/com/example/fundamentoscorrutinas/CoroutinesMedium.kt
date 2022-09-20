package com.example.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 20/9/22 at 11:02
 * More info: zambranomoya74@gmail.com
 ****/

fun main() {
    // dispatchers()
    // nested()
    // changeWithContext()
    basicFlows()
}

// Para código asincrono que retorna múltiples valores

fun basicFlows() {
    newTopic("Flows Básicos")
    runBlocking {
        launch { getDataByFlows().collect{ println(it) } }

        launch {
            (1..50).forEach {
                delay(someTime()/10)
                println("Tarea 2....")
            }
        }
    }
}

fun getDataByFlows(): Flow<Float> {
    return flow {
        (1..5).forEach {
            println("Procesando datos....")
            delay(someTime())
            emit(20 + it + Random.nextFloat())

        }
    }
}

fun changeWithContext() {
    runBlocking {
        newTopic("withContext")
        startMsg()
        withContext(newSingleThreadContext("JOZAMO")){
            startMsg()
            delay(someTime())
            println("con withContext")
            endMsg()
        }
        endMsg()
    }
}

fun nested() {
    runBlocking {
        newTopic("Anidar")

        val job = launch {
            startMsg()

            launch {
                startMsg()
                delay(someTime())
                println("Otra tarea")
                endMsg()
            }

            launch(Dispatchers.IO) {
                startMsg()

                launch(newSingleThreadContext("jzm")) {
                    startMsg()

                    println("JZM")
                    endMsg()
                }

                delay(someTime())
                println("Dispatchers.IO")
                endMsg()
            }

            var sum = 0
            (1..100).forEach {
                sum += it
                delay(someTime()/100)
            }

            println("Sum: $sum")

            endMsg()
        }

        delay(someTime() / 2)
        // Al cancelar el padre se cancelan todos los hijos
        job.cancel()
        println("Job cancelado.....")
    }
}

fun dispatchers() {
    runBlocking {
        newTopic("Dispatchers")
        launch {
            startMsg()
            println("None")
            endMsg()
        }

        // Para lectura y escritura a base de datos
        launch(Dispatchers.IO) {
            startMsg()
            println("IO")
            endMsg()
        }

        // Cuando no hay que compartir datos con otras corrutinas
        launch(Dispatchers.Unconfined) {
            startMsg()
            println("Unconfined")
            endMsg()
        }
        // Solo para android tareas rápidas asociadas con e cambio de la interfaz
    /*    launch(Dispatchers.Main) {
            startMsg()
            println("Main")
            endMsg()
        }*/

        // Usado para procesos intensivos de la cpu
        launch(Dispatchers.Default) {
            startMsg()
            println("Default")
            endMsg()
        }

        launch(Dispatchers.Unconfined) {
            startMsg()
            println("Unconfined")
            endMsg()
        }

        launch(newSingleThreadContext("Corrutina personalizada")) {
            startMsg()
            println("MI corrutina personalizada con un dispatchers")
            endMsg()
        }

        newSingleThreadContext("Personalizada 2").use { myContext ->
            launch(myContext) {
                startMsg()
                println("Mi corrutina personalizada con un dispatcher 2")
                endMsg()
            }
        }

    }
}
