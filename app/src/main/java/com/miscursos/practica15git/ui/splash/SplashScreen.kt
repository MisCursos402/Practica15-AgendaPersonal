package com.miscursos.practica15git.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Elegant professional Crimson / Ruby Red palette
val PrimaryRed = Color(0xFFDC2626)       // Crimson Red 600
val DeepRed = Color(0xFFB91C1C)          // Deep Ruby Red 700
val DarkRed = Color(0xFF991B1B)          // Dark Maroon Red 800
val LightRedBg = Color(0xFFFEF2F2)       // Ultra-soft red blush 50
val RedBorder = Color(0xFFFECACA)        // Soft red border 200
val TextMuted = Color(0xFF4B5563)        // Neutral Slate 600

@Composable
fun SplashScreen(
    durationMillis: Long = 5000L,
    onTimeout: () -> Unit
) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }
    val progress = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }
        launch {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = durationMillis.toInt(), easing = LinearEasing)
            )
        }
        delay(durationMillis.milliseconds)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFAFAFA),
                        LightRedBg.copy(alpha = 0.5f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Subtle ambient decorative glow rings in warm soft red
        Box(
            modifier = Modifier
                .size(340.dp)
                .offset(y = (-150).dp)
                .alpha(0.12f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(PrimaryRed, Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = 120.dp, y = 200.dp)
                .alpha(0.08f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(DeepRed, Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Elegant top tag badge in Red
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LightRedBg,
                border = BorderStroke(1.dp, RedBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = PrimaryRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ORGANIZADOR PERSONAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = DeepRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Professional Hero Emblem (White card + Red gradient logo with soft shadow)
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .size(136.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(38.dp),
                        ambientColor = PrimaryRed.copy(alpha = 0.25f),
                        spotColor = PrimaryRed.copy(alpha = 0.35f)
                    )
                    .clip(RoundedCornerShape(38.dp))
                    .background(Color.White)
                    .border(
                        BorderStroke(
                            2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White,
                                    RedBorder,
                                    PrimaryRed.copy(alpha = 0.25f)
                                )
                            )
                        ),
                        shape = RoundedCornerShape(38.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Central circular icon badge in Crimson Gradient
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PrimaryRed, DeepRed, DarkRed)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Agenda",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Mini floating badge (Notes)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 8.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.5.dp, RedBorder, CircleShape)
                        .shadow(4.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = "Notas",
                        tint = PrimaryRed,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Mini floating badge (Alarm)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 8.dp, y = (-8).dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "Alarma",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Title in bold Red
            Text(
                text = "Agenda & Notas",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = DeepRed,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle Description in readable Charcoal Gray with red emphasis
            Text(
                text = "Tu espacio personal para organizar eventos, guardar notas y recordar tareas importantes sin complicaciones.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Professional White Cards with Red Accents
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeatureCardWhite(
                    icon = Icons.Default.CalendarMonth,
                    title = "Agenda",
                    subtitle = "Eventos",
                    modifier = Modifier.weight(1f)
                )
                FeatureCardWhite(
                    icon = Icons.Default.EditNote,
                    title = "Notas",
                    subtitle = "Apuntes",
                    modifier = Modifier.weight(1f)
                )
                FeatureCardWhite(
                    icon = Icons.Default.Alarm,
                    title = "Alarmas",
                    subtitle = "Avisos",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Red Progress Bar Indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PrimaryRed,
                    trackColor = RedBorder.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Iniciando tu agenda...",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepRed.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Professional Button in Crimson Red Gradient
            Button(
                onClick = onTimeout,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(48.dp)
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = PrimaryRed)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryRed, DeepRed)
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Comenzar ahora",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "v1.1 • Base de datos SQLite • Jetpack Compose",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

@Composable
fun FeatureCardWhite(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, RedBorder.copy(alpha = 0.7f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(LightRedBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryRed,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DeepRed
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
