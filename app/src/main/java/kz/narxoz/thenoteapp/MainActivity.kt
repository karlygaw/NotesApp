package kz.narxoz.thenoteapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kz.narxoz.thenoteapp.database.BookDatabase
import kz.narxoz.thenoteapp.ui.theme.BookScreen
import kz.narxoz.thenoteapp.ui.theme.BottomNavigationBar
import kz.narxoz.thenoteapp.viewmodels.BookViewModel
import kz.narxoz.thenoteapp.viewmodels.BookViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels {
        BookViewModelFactory(BookDatabase.getDataBase(this).studentDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Создаем NavController для управления навигацией
            val navController = rememberNavController()

            // Выводим основной экран с навигацией
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "list", // начальный экран
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                ) {
                    // Экран "Книги"
                    composable("list") {
                        BookScreen(viewModel = viewModel)
                    }
                    // Добавьте другие экраны, например:
                    composable("problems") {
                        // Ваш экран "Читаю сейчас"
                    }
                    composable("account") {
                        // Ваш экран "Мой аккаунт"
                    }
                }
            }
        }
    }
}
