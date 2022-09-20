package com.example.fundamentoscorrutinas

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.random.Random

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 15/9/22 at 16:28
 * More info: zambranomoya74@gmail.com
 ****/
private const val SEPARATOR = "===================="

fun main() {
//    lambda()
//
//    multiLambda(2,3) { result ->
//        println(result)
//    }
//
//    threads()

    //coroutinesVsThreads()

    sequences()


}

fun sequences() {
    newTopic("Sequences")
    getDatabySeq().forEach { println("${it}º") }
}

fun getDatabySeq(): Sequence<Float> {
    return sequence {
        (1..5).forEach {
            println("Procesando datos....")
            Thread.sleep(someTime())
            yield(20 + it + Random.nextFloat())

        }
    }
}


fun coroutinesVsThreads() {
    newTopic("Coroutines vs Threads")

    runBlocking {
        (1..1_000_000).forEach {
            launch {
                delay(someTime())
                print('*')
            }
        }
    }

//    (1..1_000_000).forEach{
//        thread {
//            Thread.sleep(someTime())
//            print('*')
//        }
//    }
}

fun newTopic(topic: String) {
    println("\n$SEPARATOR $topic $SEPARATOR\n")
}

fun threads() {
    newTopic("Threads")
    println("Thead: ${multiThread(2, 3)}")
    
    multiThreadLambda(2, 3){
        println("Thread+Lambda: $it")
    }
}

fun multiThread(x: Int, y: Int): Int {
    var result = 0

    thread {
        Thread.sleep(someTime())
        result = x * y
    }
    Thread.sleep(2_100)
    return result
}
fun multiThreadLambda(x: Int, y: Int, callback: (result: Int) -> Unit) {
    var result: Int

    thread {
        Thread.sleep(someTime())
        result = x * y
        callback(result)
    }

}

fun someTime(): Long = Random.nextLong(500, 2000)

fun multiLambda(x: Int, y: Int, callback: (result: Int) -> Unit) {
    callback(x*y)
}

fun lambda() {
    newTopic("Lambda")
    println(multi(2,3))
}

fun multi(x: Int, y: Int) = x * y


