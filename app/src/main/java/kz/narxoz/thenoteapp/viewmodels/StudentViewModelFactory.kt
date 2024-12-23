package kz.narxoz.thenoteapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.narxoz.thenoteapp.database.StudentDao

class StudentViewModelFactory(private val studentDao: StudentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(studentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}