package com.example.dragonki.ui.detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dragonki.R

@Composable
fun DetailScreen(
    characterId: Int,
    navController: NavController,
    vm: DetailViewModel = viewModel()
) {
    val character by vm.character.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    LaunchedEffect(characterId) { vm.load(characterId) }

    // Dimensiones desde resources
    val topPadding    = dimensionResource(R.dimen.top_padding)
    val spacer8       = dimensionResource(R.dimen.spacing_small)
    val spacer12      = dimensionResource(R.dimen.spacing_medium)

    // Colores desde resources
    val overlay30     = colorResource(R.color.overlay_black_30)
    val overlay70     = colorResource(R.color.overlay_black_70)
    val gold          = colorResource(R.color.gold)
    val orange        = colorResource(R.color.orange)
    val cyan          = colorResource(R.color.cyan)
    val white         = colorResource(R.color.white)
    val black         = colorResource(R.color.black)
    val transformBg   = colorResource(R.color.transform_bg)

    // Strings y placeholder
    val descBack      = stringResource(R.string.desc_back_button)
    val noDescription = stringResource(R.string.no_description)
    val errorLoading  = stringResource(R.string.error_loading)
    val unknown       = stringResource(R.string.unknown_placeholder)

    // Constantes unit‐less
    val widthFraction  = 0.7f
    val aspectRatio    = 1.2f
    val glowMin        = 8f
    val glowMax        = 18f
    val glowDurationMs = 1500

    val saiyanFont = FontFamily(Font(R.font.saiyan_sans))

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = gold)
        }
        return
    }

    character?.let { ch ->
        // Glow animación única
        val glow by rememberInfiniteTransition().animateFloat(
            initialValue = glowMin,
            targetValue  = glowMax,
            animationSpec = infiniteRepeatable(
                animation   = tween(glowDurationMs),
                repeatMode  = RepeatMode.Reverse
            )
        )

        Box(Modifier.fillMaxSize()) {
            // Fondo
            Image(
                painter = painterResource(R.drawable.nube),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.4f
            )

            // Botón volver
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(spacer8)
                    .size(spacer12 * 3)
            ) {
                Icon(
                    painter = painterResource(R.drawable.bola),
                    contentDescription = descBack,
                    tint = Color.Unspecified
                )
            }

            // Contenido
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding, start = spacer12, end = spacer12),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen proporcional
                item {
                    val screenW = LocalConfiguration.current.screenWidthDp.dp
                    val imgW = screenW * widthFraction
                    val imgH = imgW * aspectRatio

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imgH),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ch.imageUrl,
                            contentDescription = ch.name,
                            modifier = Modifier.size(width = imgW, height = imgH),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Nombre con glow
                item {
                    Text(
                        text = ch.name.uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = saiyanFont,
                        color = white,
                        modifier = Modifier.padding(spacer8),
                        style = TextStyle(shadow = Shadow(black, Offset(2f,2f), glow))
                    )
                }

                // Stats
                item {
                    Spacer(Modifier.height(spacer8))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(containerColor = overlay70),
                        shape    = RoundedCornerShape(spacer12)
                    ) {
                        Column(Modifier.padding(spacer12)) {
                            val stats = listOf(
                                R.string.label_ki to "${ch.ki}",
                                R.string.label_maxki to "${ch.maxKi}",
                                R.string.label_affiliation to (ch.affiliation ?: stringResource(R.string.no_affiliation)),
                                R.string.label_race to (ch.race ?: unknown),
                                R.string.label_gender to (ch.gender ?: unknown)
                            )
                            val cols = listOf(gold, orange, cyan, cyan, white)
                            stats.forEachIndexed { i, (res, v) ->
                                Text(
                                    text = stringResource(res) + " $v",
                                    color = cols[i],
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.bodyMedium.merge(
                                        TextStyle(shadow = Shadow(black, Offset(1f,1f), 2f))
                                    )
                                )
                            }
                        }
                    }
                }

                // Descripción
                item {
                    Spacer(Modifier.height(spacer12))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(containerColor = white.copy(alpha = 0.9f)),
                        shape    = RoundedCornerShape(spacer12)
                    ) {
                        Text(
                            text = ch.description ?: noDescription,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(spacer12),
                            lineHeight = 20.sp
                        )
                    }
                }

                // Transformaciones
                if (!ch.transformations.isNullOrEmpty()) {
                    item {
                        Spacer(Modifier.height(spacer12))
                        Text(
                            text = stringResource(R.string.transformations),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = black,
                            modifier = Modifier.padding(bottom = spacer8)
                        )
                    }
                    items(ch.transformations!!) { tf ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spacer8),
                            colors = CardDefaults.cardColors(containerColor = transformBg),
                            shape  = RoundedCornerShape(spacer12)
                        ) {
                            Row(
                                Modifier.padding(spacer12),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = tf.imageUrl,
                                    contentDescription = tf.name,
                                    modifier = Modifier.size(spacer12 * 6),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(Modifier.width(spacer8))
                                Column {
                                    Text(tf.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(
                                        text = stringResource(R.string.label_ki) + " ${tf.ki}",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = errorLoading,
                color = white
            )
        }
    }
}
