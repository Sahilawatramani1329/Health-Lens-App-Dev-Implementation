package com.example.healthlens3

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthlens3.ui.theme.Healthlens3Theme
import com.google.firebase.FirebaseApp
import fourthactivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Healthlens3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            firstactivity(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }
        composable("login") {
            secondactivity(
                onLoginSuccess = { userId ->
                    navController.navigate("userProfileAndHistory/$userId") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            thirdactivity { userId ->
                navController.navigate("userProfileAndHistory/$userId") {
                    popUpTo("welcome") { inclusive = true }
                }
            }
        }
        composable("userProfileAndHistory/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            fourthactivity(userId = userId, onUploadClick = { navController.navigate("fifthactivity") })
        }


        // Define the composable for fifthactivity
        composable("fifthactivity") {
            fifthactivity()
        }
        // You can add other composables here as needed
    }
}
