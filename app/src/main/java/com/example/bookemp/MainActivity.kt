package com.example.bookemp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookemp.ui.screens.*
import com.example.bookemp.ui.theme.BookempTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TELJES KÉPERNYŐ ALAP
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BookempTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("mainMenu") {
                        MainMenuScreen(navController = navController)
                    }
                    composable("libraryList") {
                        LibraryListScreen(navController = navController, onBack = {
                            navController.popBackStack()
                        })
                    }
                    composable("createLibrary") {
                        CreateLibraryScreen(onLibraryCreated = {
                            navController.popBackStack()
                        })
                    }
                    composable("bookList/{libraryId}/{libraryName}") { backStackEntry ->
                        val libraryId = backStackEntry.arguments?.getString("libraryId")
                        val libraryName = backStackEntry.arguments?.getString("libraryName")
                        if (libraryId != null && libraryName != null) {
                            BookListScreen(libraryId = libraryId, libraryName = libraryName, navController = navController)
                        }
                    }
                    composable("editBook/{libraryId}/{bookId}/{currentTitle}/{currentAuthor}") { backStackEntry ->
                        val libraryId = backStackEntry.arguments?.getString("libraryId")
                        val bookId = backStackEntry.arguments?.getString("bookId")
                        val currentTitle = backStackEntry.arguments?.getString("currentTitle")
                        val currentAuthor = backStackEntry.arguments?.getString("currentAuthor")
                        if (libraryId != null && bookId != null && currentTitle != null && currentAuthor != null) {
                            EditBookScreen(
                                libraryId = libraryId,
                                bookId = bookId,
                                currentTitle = currentTitle,
                                currentAuthor = currentAuthor,
                                navController = navController
                            )
                        }
                    }
                    composable("createBook/{libraryId}") { backStackEntry ->
                        val libraryId = backStackEntry.arguments?.getString("libraryId")
                        if (libraryId != null) {
                            CreateBookScreen(libraryId = libraryId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
