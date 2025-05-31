package com.example.dragonki.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dragonki.R
import com.example.dragonki.data.model.CharacterSummary

@Composable
fun ListScreen(
    onCharacterClick: (CharacterSummary) -> Unit,
    vm: ListViewModel = viewModel()
) {
    val isLoading  by vm.isLoading.collectAsState()
    val characters by vm.characters.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // fondo y capa oscura
        Image(
            painter = painterResource(id = R.drawable.nube),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // pestaña con logo
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(Color(0xFFFFC107))
                .border(2.dp, Color.Red, RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.logodbz),
                contentDescription = stringResource(R.string.logo_desc),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.6f)
                    .aspectRatio(3f)
            )
        }

        when {
            isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Yellow)
                }
            }
            characters.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_characters),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    items(characters) { ch ->
                        CharacterCard(ch, onCharacterClick)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: CharacterSummary,
    onClick: (CharacterSummary) -> Unit
) {
    val gold       = Color(0xFFFFC107)
    val saiyanFont = FontFamily(Font(R.font.saiyan_sans))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .border(2.dp, gold, MaterialTheme.shapes.medium)
            .clickable { onClick(character) },
        colors    = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model               = character.imageUrl,
                contentDescription  = character.name,
                contentScale        = ContentScale.Fit,
                modifier            = Modifier
                    .size(200.dp)
                    .padding(6.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text      = character.name,
                fontFamily= saiyanFont,
                fontSize  = 32.sp,
                color     = Color.White,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth()
            )

            Text(
                text      = character.affiliation
                    ?: stringResource(R.string.no_affiliation),
                fontFamily= saiyanFont,
                fontSize  = 20.sp,
                color     = Color.Yellow,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth()
            )
        }
    }
}
