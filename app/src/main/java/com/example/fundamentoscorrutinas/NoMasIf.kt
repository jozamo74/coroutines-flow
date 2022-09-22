package com.example.fundamentoscorrutinas

/****
 * Project: Fundamentos Corrutinas
 * From: com.example.fundamentoscorrutinas
 * Created by José Zambrano Moya on 22/9/22 at 12:32
 * More info: zambranomoya74@gmail.com
 ****/

fun main() {
    val fruta = "MANZANA"
    print("$fruta tiene ${noMasIf(fruta.lowercase())}")
}

fun noMasIf(fruta: String): String {
    val map : Map<String, String> = mapOf(
        "fresa" to "65 calorías",
        "mango" to "57 calorías",
        "manzana" to "52 calorías",
        "platano" to "90 calorías"
    )

    return map[fruta] ?: "N/A"

}
