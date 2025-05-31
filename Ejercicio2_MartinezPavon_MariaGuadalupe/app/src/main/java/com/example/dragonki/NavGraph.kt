package com.example.dragonki

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dragonki.data.model.CharacterSummary
import com.example.dragonki.ui.detail.DetailScreen
import com.example.dragonki.ui.list.ListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ListScreen(onCharacterClick = { ch: CharacterSummary ->
                navController.navigate("detail/${ch.id}")
            })
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            DetailScreen(characterId = id, navController = navController)
        }
    }
}
