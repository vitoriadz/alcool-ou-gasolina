package com.example.exemplosimplesdecompose.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.R
import com.example.exemplosimplesdecompose.viewmodel.PostoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlcoolGasolinaPreco(navController: NavHostController, viewModel: PostoViewModel) {
    var alcool by remember { mutableStateOf("") }
    var gasolina by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    // Switch state vem do ViewModel (persistido em SharedPreferences)
    val alcoolSelecionado by viewModel.alcoolSelecionado.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    actions = {
                        IconButton(onClick = { navController.navigate("lista") }) {
                            Icon(Icons.Filled.List, contentDescription = stringResource(R.string.lista_postos))
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = alcool,
                    onValueChange = { alcool = it },
                    label = { Text(stringResource(R.string.preco_alcool)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = gasolina,
                    onValueChange = { gasolina = it },
                    label = { Text(stringResource(R.string.preco_gasolina)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                // Switch com legenda e estado persistido
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.regra_75),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (alcoolSelecionado)
                                    stringResource(R.string.usar_alcool)
                                else
                                    stringResource(R.string.usar_gasolina),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Switch(
                            modifier = Modifier.semantics { contentDescription = "Álcool ou Gasolina" },
                            checked = alcoolSelecionado,
                            onCheckedChange = { viewModel.setSwitchState(it) },
                            thumbContent = {
                                if (alcoolSelecionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize)
                                    )
                                }
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        val a = alcool.replace(",", ".").toDoubleOrNull()
                        val g = gasolina.replace(",", ".").toDoubleOrNull()
                        resultado = when {
                            a == null || g == null -> "Preencha os valores corretamente."
                            g == 0.0 -> "Preço da gasolina não pode ser zero."
                            else -> {
                                val razao = a / g
                                if (razao <= 0.7)
                                    "✅ Vale mais a pena usar Álcool (${String.format("%.1f", razao * 100)}%)"
                                else
                                    "⛽ Vale mais a pena usar Gasolina (${String.format("%.1f", razao * 100)}%)"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.calcular))
                }

                if (resultado.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = resultado,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                OutlinedButton(
                    onClick = { navController.navigate("lista") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.ver_postos))
                }
            }
        }
    }
}