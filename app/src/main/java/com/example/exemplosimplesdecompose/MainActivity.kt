package com.example.exemplosimplesdecompose

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exemplosimplesdecompose.ui.theme.ExemploSimplesDeComposeTheme
import com.example.exemplosimplesdecompose.view.AlcoolGasolinaPreco
import com.example.exemplosimplesdecompose.view.DetalhesPosto
import com.example.exemplosimplesdecompose.view.FormularioPosto
import com.example.exemplosimplesdecompose.view.ListaDePostos
import com.example.exemplosimplesdecompose.view.Welcome
import com.example.exemplosimplesdecompose.viewmodel.PostoViewModel

class MainActivity : ComponentActivity() {

    val viewModel: PostoViewModel by viewModels()

    // Solicita permissão de localização ao iniciar
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* permissões tratadas nas views */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        enableEdgeToEdge()
        setContent {
            ExemploSimplesDeComposeTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: PostoViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            Welcome(navController)
        }
        composable("mainalcgas") {
            AlcoolGasolinaPreco(navController, viewModel)
        }
        composable("lista") {
            ListaDePostos(navController, viewModel)
        }
        composable(
            route = "formulario?postoId={postoId}",
            arguments = listOf(navArgument("postoId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStack ->
            val postoId = backStack.arguments?.getString("postoId")
            FormularioPosto(navController, viewModel, postoId)
        }
        composable(
            route = "detalhes/{postoId}",
            arguments = listOf(navArgument("postoId") { type = NavType.StringType })
        ) { backStack ->
            val postoId = backStack.arguments!!.getString("postoId")!!
            DetalhesPosto(navController, viewModel, postoId)
        }
    }
}