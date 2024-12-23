package kz.narxoz.thenoteapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(student: Student)

    @Query("SELECT * FROM Student")
    fun getAll(): Flow<List<Student>>

    @Query("DELETE FROM Student WHERE id = :studentId")
    fun deleteById(studentId: Int)

    @Query("UPDATE Student SET name = :studentName, phone = :studentPhone WHERE id = :studentId")
    fun updateStudent(studentId: Int, studentName: String, studentPhone: Long)

}

