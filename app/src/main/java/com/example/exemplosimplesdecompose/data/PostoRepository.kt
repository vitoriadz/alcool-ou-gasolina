package com.example.exemplosimplesdecompose.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class PostoRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("postos_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_POSTOS = "postos_list"
        private const val KEY_SWITCH = "switch_alcool_selecionado"
    }

    // ── Switch state ──────────────────────────────────────────────────────────

    fun saveSwitchState(alcoolSelecionado: Boolean) {
        prefs.edit().putBoolean(KEY_SWITCH, alcoolSelecionado).apply()
    }

    fun getSwitchState(): Boolean = prefs.getBoolean(KEY_SWITCH, true)

    // ── CRUD Postos ───────────────────────────────────────────────────────────

    fun getPostos(): List<Posto> {
        val json = prefs.getString(KEY_POSTOS, "[]") ?: "[]"
        val array = JSONArray(json)
        return (0 until array.length()).map { Posto.fromJson(array.getJSONObject(it)) }
    }

    fun savePosto(posto: Posto) {
        val list = getPostos().toMutableList()
        val idx = list.indexOfFirst { it.id == posto.id }
        if (idx >= 0) list[idx] = posto else list.add(posto)
        persistList(list)
    }

    fun deletePosto(id: String) {
        val list = getPostos().toMutableList()
        list.removeAll { p -> p.id == id }
        persistList(list)
    }

    private fun persistList(list: List<Posto>) {
        val array = JSONArray().apply { list.forEach { put(it.toJson()) } }
        prefs.edit().putString(KEY_POSTOS, array.toString()).apply()
    }
}