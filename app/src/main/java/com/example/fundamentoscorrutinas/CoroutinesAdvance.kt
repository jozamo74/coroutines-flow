package com.example.fundamentoscorrutinas

import kotlinx.coroutines.runBlocking

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 22/9/22 at 16:27
 * More info: zambranomoya74@gmail.com
 ****/

fun main() {
    basicChanel()
}

// Forma de transmitir flojo de valores entre coroutines
fun basicChanel() {
    runBlocking {
        newTopic("Canal básico")
    }
}
