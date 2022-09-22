package com.example.fundamentoscorrutinas

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 20/9/22 at 17:12
 * More info: zambranomoya74@gmail.com
 ****/
fun main() {
    //coldFlow()
    // cancelFlow()
    // flowOperators()
    // terminalFlowOperator()
    // bufferFlow()
    // conflationFlow()
    // multiFlow()
    // flatFows()
    // flowException()
    completion()


}

fun completion() {
    runBlocking {
        newTopic("Fin de un flujo(onCompletion)")
        getCitiesFlow()
            .onCompletion { println("Quitar el progressBar...") }
            .collect { println(it) }

        getMatchResultsFlow()
            .onCompletion { println("Mostrar las estadisticas...")  }
            .catch { emit("Error: $this") }
            .collect { println(it) }

        newTopic("Cancelar Flow")
        getDataByFlowStatic()
            .onCompletion { println("Ya no interesa al usuario....") }
            .cancellable()
            .collect {
                if (it > 22.5f) cancel()
                println(it)
            }
    }
}

fun flowException() {
    runBlocking {
        newTopic("Control de errores")
        newTopic("Try/Catch")
        /*try {
            getMatchResultsFlow()
                .collect{
                    println(it)
                    if (it.contains("2")) throw Exception ("Habian acordado 1-1 : V")
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

        newTopic("Transparencia")
        getMatchResultsFlow()
            .catch {
                emit("Error: $this")
            }
            .collect {
                println(it)
                if (!it.contains("-")) println("Notifica al programador")
            }

    }
}

fun flatFows() {
    runBlocking {
        newTopic("Flujos de aplanamiento")
        getCitiesFlow()
            .flatMapConcat { city -> //Flow<Flow<TYPE>>
                getDataFlatFlow(city)

            }
            .map { setFormat(it) }
            .collect { println(it) }
    }
}

fun getDataFlatFlow(city: String): Flow<Float> = flow {
    (1..3).forEach {
        println("Temperatura de ayer en $city")
        emit(Random.nextInt(10, 30).toFloat())

        println("Temperatura actual en $city")
        delay(100)
        emit(20 + it + Random.nextFloat())
    }
}

fun getCitiesFlow(): Flow<String> = flow {
    listOf("Santander", "Barcelona", "Madrid")
        .forEach{ city ->
            println("\nConsultando ciudad")
            delay(1_000)
            emit(city)
        }
}

fun multiFlow() {
    runBlocking {
        newTopic("Zip & Combine")
        getDataByFlowStatic()
            .map { setFormat(it) }
            .combine(getMatchResultsFlow()){ degrees, result ->
            //.zip(getMatchResultsFlow()){degrees, result ->
                "$result with $degrees"
            }
            .collect { println(it) }
    }
}

fun conflationFlow() {
    runBlocking {
        newTopic("Fusión")
        val time = measureTimeMillis {
            getMatchResultsFlow()
                .conflate()
                .collect {
                    delay(100)
                    println(it)
                }
        }
        println("Time: ${time}ms")
    }
}

fun getMatchResultsFlow(): Flow<String> {
    return flow {
        var homeTeam = 0
        var awayTeam = 0
        (0..45).forEach {
            println("Minuto: $it")
            delay(50)
            homeTeam += Random.nextInt(0, 21) / 20
            awayTeam += Random.nextInt(0, 21) / 20
            emit("$homeTeam-$awayTeam")

            if (homeTeam == 2 || awayTeam == 2) throw Exception("Habian acordado 1-1:v")
        }
    }
}

fun bufferFlow() {
    runBlocking {

        newTopic("Buffer para Flow")
        val time = measureTimeMillis {
            getDataByFlowStatic()
                .map { setFormat(it) }
                .buffer()
                .collect {
                    delay(500)
                    println(it)
                }
        }
        println("Time: ${time}ms")
    }
}

fun terminalFlowOperator() {
    runBlocking {
        newTopic("Operadores Flow Terminales")
        newTopic("List")
        val list = getDataByFlows()
            .toList()
        println("List: $list")

        newTopic("Single")
        val single = getDataByFlows()
           // .take(1)
          //  .single()
        println("Single: $single")

        newTopic("First")
        val first = getDataByFlows()
            .first()
        println("First: $first")

        newTopic("Last")
        val last = getDataByFlows()
            .last()
        println("First: $last")

        newTopic("Reduce")
        val saving = getDataByFlows()
            .reduce{ accumulator, value ->
                println("Accumulator: $accumulator")
                println("Value: $value")
                println("Current saving: ${accumulator + value}")
                accumulator + value

            }

        println("Saving: $saving")

        newTopic("Fold")
        val lastSaving = saving
        val totalSaving = getDataByFlows()
            .fold(lastSaving) { accumulator, value ->
                println("Accumulator: $accumulator")
                println("Value: $value")
                println("Current saving: ${accumulator + value}")
                accumulator + value
            }
        println("TotalSaving: $totalSaving")

    }
}

fun flowOperators() {
    runBlocking {
        newTopic("Operadores Flow Inermediarios")
        newTopic("Map")
        getDataByFlows()
            .map {
                //Se puede ejecutar operación suspendida dentro del bloque de código
                // setFormat(it)
                setFormat(convertCelsToFahr(it), "F")
            }
            //.collect { println(it) }
        newTopic("Filter")
        getDataByFlows()
            .filter {
                it < 23
            }
            .map {
                setFormat(it)
            }
            //.collect { println(it) }

        newTopic("Transfort")
        getDataByFlows()
            .transform {
                emit(setFormat(it))
                emit(setFormat(convertCelsToFahr(it), "F"))
            }
            //.collect { println(it) }

        newTopic("Take")
        getDataByFlows()
            .take(3)
            .map { setFormat(it) }
            .collect { println(it) }
    }
}

fun convertCelsToFahr(cels: Float): Float = ((cels * 9) / 5) + 32

fun setFormat(temp: Float, degree: String = "C"): String =
    String.format(Locale.getDefault(), "%.1fº$degree", temp)

fun cancelFlow() {
    runBlocking {
        newTopic("Cancelar Flow")
        val job = launch {
            getDataByFlows().collect { println(it) }
        }
        delay(someTime()*2)
        job.cancel()
    }
}

fun coldFlow() {
    newTopic("Flows are Cold")
    runBlocking {
        val dataFlow = getDataByFlows()
        println("esperando")
        delay(someTime())
        dataFlow.collect{ println(it) }
    }
}

fun getDataByFlowStatic(): Flow<Float> {
    return flow {
        (1..5).forEach {
            println("Procesando datos....")
            delay(300)
            emit(20 + it + Random.nextFloat())

        }
    }
}
