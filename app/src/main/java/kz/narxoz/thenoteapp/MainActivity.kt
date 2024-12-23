package kz.narxoz.thenoteapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kz.narxoz.thenoteapp.database.StudentDatabase
import kz.narxoz.thenoteapp.ui.theme.StudentScreen
import kz.narxoz.thenoteapp.viewmodels.StudentViewModel
import kz.narxoz.thenoteapp.viewmodels.StudentViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: StudentViewModel by viewModels {
        StudentViewModelFactory(StudentDatabase.getDataBase(this).studentDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentScreen(viewModel = viewModel)
        }
    }
}
