package kz.narxoz.thenoteapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Student")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val studentName: String,
    @ColumnInfo(name = "phone")
    val studentPhone: Long
)

