package com.example.exemplosimplesdecompose.data

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Posto(
    val id: String = java.util.UUID.randomUUID().toString(),
    val nome: String,
    val precoAlcool: Double = 0.0,
    val precoGasolina: Double = 0.0,
    val coordenadas: Coordenadas = Coordenadas(41.40338, 2.17403),
    val temLocalizacaoReal: Boolean = false,
    val dataCadastro: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
) {
    // Mantém o construtor secundário original (só nome → coordenadas padrão de Fortaleza)
    constructor(nome: String) : this(
        nome = nome,
        coordenadas = Coordenadas(41.40338, 2.17403),
        temLocalizacaoReal = false
    )

    // Construtor secundário original com coordenadas explícitas
    constructor(nome: String, coordenadas: Coordenadas) : this(
        nome = nome,
        coordenadas = coordenadas,
        temLocalizacaoReal = true
    )

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("nome", nome)
        put("precoAlcool", precoAlcool)
        put("precoGasolina", precoGasolina)
        put("latitude", coordenadas.latitude)
        put("longitude", coordenadas.longitude)
        put("temLocalizacaoReal", temLocalizacaoReal)
        put("dataCadastro", dataCadastro)
    }

    companion object {
        fun fromJson(json: JSONObject): Posto = Posto(
            id = json.optString("id", java.util.UUID.randomUUID().toString()),
            nome = json.optString("nome", ""),
            precoAlcool = json.optDouble("precoAlcool", 0.0),
            precoGasolina = json.optDouble("precoGasolina", 0.0),
            coordenadas = Coordenadas(
                latitude = json.optDouble("latitude", 41.40338),
                longitude = json.optDouble("longitude", 2.17403)
            ),
            temLocalizacaoReal = json.optBoolean("temLocalizacaoReal", false),
            dataCadastro = json.optString("dataCadastro", "")
        )
    }
}