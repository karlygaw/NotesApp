package kz.narxoz.thenoteapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "image")
    val image: Int? = null,  // Assuming image is a drawable resource ID

    @ColumnInfo(name = "reviews")
    val reviews: String? = null,

    @ColumnInfo(name = "progress")
    val progress: Float,

    @ColumnInfo(name = "genre")
    val genre: String
)

