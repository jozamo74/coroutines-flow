package com.example.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 16/9/22 at 18:02
 * More info: zambranomoya74@gmail.com
 ****/

fun main() {
    //globalScope()
    //suspendFun()

    newTopic("Constructores de corrutinas")

    //cRunBlocking()
    //cLaunch()
    //cAsync()
    //job()
    //deferred()
    cProduct()
    //readLine()
}

fun cProduct() = runBlocking{
    newTopic("Produce")
    val names = produceNames()
    names.consumeEach { println(it) }

}

fun CoroutineScope.produceNames():ReceiveChannel<String> = produce {
    (1..5).forEach { send("name: $it") }
}

fun deferred() {
    runBlocking {
        newTopic("Deferred")
        val deferred = async {
            startMsg()
            delay(someTime())
            println("Deferred....")
            endMsg()
            multi(5,2)
        }
        println("Deferred: $deferred")
        println("Valor del Deferred: ${deferred.await()}")

        //Constructor ideal para la devolución de un valor
        val result = async {
            multi(3, 3)
        }.await()
        println("Result: $result")
    }
}

fun job() {
    runBlocking {
        newTopic("Job")
        val job = launch {
            startMsg()
            //delay(someTime())
            delay(2_100)
            println("Job...")
            endMsg()
        }
        println("Job: $job")
        //delay(4_000)
        println("isActive: ${job.isActive}")
        println("isCancelled: ${job.isCancelled}")
        println("isCompleted: ${job.isCompleted}")

        delay(someTime())
        println("Tarea cancelada")

        job.cancel()

        println("isActive: ${job.isActive}")
        println("isCancelled: ${job.isCancelled}")
        println("isCompleted: ${job.isCompleted}")


    }
}

// Espera el resultado de un valor
fun cAsync() {
    newTopic("Async")
    runBlocking {
        val result =async {
            startMsg()
            delay(someTime())
            println("async...")
            endMsg()
            "hola pixón"
        }
        println("Result: ${result.await()}")
    }
}

// Para tareas que no necesiten devolver valor o resultado
// Por ejemplo para la recopilación de datos
fun cLaunch() {
    newTopic("Launch")
    runBlocking {
        launch {
            startMsg()
            delay(someTime())
            endMsg()
        }
    }

}

fun cRunBlocking() {
    newTopic("RunBlocking")
    runBlocking {

        startMsg()
        delay(someTime())
        endMsg()
    }
}

fun suspendFun() {
    newTopic("Suspend")
    Thread.sleep(someTime())
    GlobalScope.launch {

        delay(someTime())
    }
}

// esta en ejecución mientras la aplicación este viva (mientras no finalice el proceso)
fun globalScope() {
    newTopic("Global Scope")
    GlobalScope.launch {
        startMsg()
        println("Mi corrutina")
        endMsg()
    }
}

fun startMsg(){
    println("Comenzando corrutina -${Thread.currentThread().name}-")
}
fun endMsg(){
    println("Corrutina -${Thread.currentThread().name} Finalizada-")
}
