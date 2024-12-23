package kz.narxoz.thenoteapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kz.narxoz.thenoteapp.database.BookDatabase

import kz.narxoz.thenoteapp.ui.theme.BookScreen
import kz.narxoz.thenoteapp.viewmodels.BookViewModel
import kz.narxoz.thenoteapp.viewmodels.BookViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels {
        BookViewModelFactory(BookDatabase.getDataBase(this).studentDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookScreen(viewModel = viewModel)
        }
    }
}
