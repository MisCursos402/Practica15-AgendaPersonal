package com.miscursos.practica15git

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.miscursos.practica15git.ui.MainScreen
import com.miscursos.practica15git.ui.theme.Practica15GitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica15GitTheme {
                MainScreen()
            }
        }
    }
}
