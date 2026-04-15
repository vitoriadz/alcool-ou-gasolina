package com.example.exemplosimplesdecompose.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.R
import com.example.exemplosimplesdecompose.data.Coordenadas
import com.example.exemplosimplesdecompose.data.Posto
import com.example.exemplosimplesdecompose.viewmodel.PostoViewModel
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPosto(
    navController: NavHostController,
    viewModel: PostoViewModel,
    postoId: String? = null
) {
    val context = LocalContext.current
    val existente = postoId?.let { viewModel.getPostoById(it) }

    var nome by remember { mutableStateOf(existente?.nome ?: "") }
    var alcool by remember { mutableStateOf(existente?.precoAlcool?.toString() ?: "") }
    var gasolina by remember { mutableStateOf(existente?.precoGasolina?.toString() ?: "") }
    // Usamos null para indicar "GPS não capturado ainda"; ao salvar usamos temLocalizacaoReal
    var coordenadas by remember {
        mutableStateOf(
            if (existente?.temLocalizacaoReal == true) existente.coordenadas else null
        )
    }
    var localizandoGps by remember { mutableStateOf(false) }
    var erroNome by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    fun capturarLocalizacao() {
        val permFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val permCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permFine == PackageManager.PERMISSION_GRANTED || permCoarse == PackageManager.PERMISSION_GRANTED) {
            localizandoGps = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                localizandoGps = false
                location?.let { coordenadas = Coordenadas(it.latitude, it.longitude) }
            }.addOnFailureListener {
                localizandoGps = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existente == null) stringResource(R.string.novo_posto)
                        else stringResource(R.string.editar_posto)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.voltar))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it; erroNome = false },
                label = { Text(stringResource(R.string.nome_posto)) },
                modifier = Modifier.fillMaxWidth(),
                isError = erroNome,
                supportingText = {
                    if (erroNome) Text(stringResource(R.string.campo_obrigatorio))
                }
            )

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

            // Localização
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.localizacao),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (coordenadas != null) {
                        Text(
                            text = "Lat: ${String.format("%.5f", coordenadas!!.latitude)}\n" +
                                    "Lng: ${String.format("%.5f", coordenadas!!.longitude)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.sem_localizacao),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { capturarLocalizacao() },
                            enabled = !localizandoGps
                        ) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.capturar_localizacao))
                        }
                        if (localizandoGps) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nome.isBlank()) {
                        erroNome = true
                        return@Button
                    }
                    val coord = coordenadas ?: Coordenadas(41.40338, 2.17403)
                    val posto = Posto(
                        id = existente?.id ?: java.util.UUID.randomUUID().toString(),
                        nome = nome.trim(),
                        precoAlcool = alcool.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        precoGasolina = gasolina.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        coordenadas = coord,
                        temLocalizacaoReal = coordenadas != null,
                        dataCadastro = existente?.dataCadastro
                            ?: java.text.SimpleDateFormat(
                                "dd/MM/yyyy HH:mm",
                                java.util.Locale.getDefault()
                            ).format(java.util.Date())
                    )
                    viewModel.salvarPosto(posto)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.salvar))
            }
        }
    }
}