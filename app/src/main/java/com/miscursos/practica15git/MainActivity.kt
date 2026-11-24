package com.miscursos.practica15git

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.miscursos.practica15git.ui.MainScreen
import com.miscursos.practica15git.ui.splash.SplashScreen
import com.miscursos.practica15git.ui.theme.Practica15GitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica15GitTheme {
                var showSplash by rememberSaveable { mutableStateOf(true) }

                AnimatedContent(
                    targetState = showSplash,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    },
                    label = "SplashToMainTransition"
                ) { isSplashActive ->
                    if (isSplashActive) {
                        SplashScreen(
                            durationMillis = 5000L,
                            onTimeout = { showSplash = false }
                        )
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}
