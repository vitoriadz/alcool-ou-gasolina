package com.example.exemplosimplesdecompose.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.R
import com.example.exemplosimplesdecompose.viewmodel.PostoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesPosto(
    navController: NavHostController,
    viewModel: PostoViewModel,
    postoId: String
) {
    val context = LocalContext.current
    val posto = viewModel.getPostoById(postoId)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (posto == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.confirmar_exclusao)) },
            text = { Text(stringResource(R.string.excluir_posto_msg, posto.nome)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletarPosto(postoId)
                    navController.popBackStack()
                }) {
                    Text(stringResource(R.string.excluir), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(posto.nome) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.voltar))
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("formulario?postoId=${posto.id}")
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.editar))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.excluir),
                            tint = MaterialTheme.colorScheme.error
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Preços
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.precos),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.alcool_label))
                        Text(
                            "R$ ${String.format("%.3f", posto.precoAlcool)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Divider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.gasolina_label))
                        Text(
                            "R$ ${String.format("%.3f", posto.precoGasolina)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Data de cadastro
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.data_cadastro),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = posto.dataCadastro,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Localização
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.localizacao),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (posto.temLocalizacaoReal) {
                        Text(
                            text = "Lat: ${String.format("%.5f", posto.coordenadas.latitude)}\n" +
                                    "Lng: ${String.format("%.5f", posto.coordenadas.longitude)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val lat = posto.coordenadas.latitude
                                val lng = posto.coordenadas.longitude
                                val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${posto.nome})")
                                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                // Fallback para qualquer app de mapas
                                val chooser = Intent.createChooser(
                                    Intent(Intent.ACTION_VIEW, uri), posto.nome
                                )
                                context.startActivity(chooser)
                            }
                        ) {
                            Icon(Icons.Filled.Map, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.abrir_mapa))
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.sem_localizacao),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}