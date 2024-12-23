package kz.narxoz.thenoteapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kz.narxoz.thenoteapp.database.Student
import kz.narxoz.thenoteapp.database.StudentDao

class StudentViewModel(private val studentDao: StudentDao) : ViewModel() {
    val allStudents: Flow<List<Student>> = studentDao.getAll()

    fun insertStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentDao.addData(student)
            } catch (e: Exception) {
                println("Error inserting student: ${e.message}")
            }
        }
    }

    fun deleteStudent(studentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentDao.deleteById(studentId)
            } catch (e: Exception) {
                println("Error deleting student: ${e.message}")
            }
        }
    }

    fun updateStudent(studentId: Int, studentName: String, studentPhone: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentDao.updateStudent(studentId, studentName, studentPhone)
            } catch (e: Exception) {
                println("Error updating student: ${e.message}")
            }
        }
    }

}

