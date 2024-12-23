package kz.narxoz.thenoteapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    // Добавить книгу
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(book: Book)

    // Получить все книги
    @Query("SELECT * FROM books")
    fun getAll(): Flow<List<Book>>

    // Удалить книгу по ID
    @Query("DELETE FROM books WHERE id = :bookId")
    fun deleteById(bookId: Int)

    // Обновить книгу по ID
    @Query("UPDATE books SET title = :bookTitle, author = :bookAuthor, description = :bookDescription, " +
            "date = :bookDate, image = :bookImage, reviews = :bookReviews, progress = :bookProgress, genre = :bookGenre WHERE id = :bookId")
    fun updateBook(
        bookId: Int,
        bookTitle: String,
        bookAuthor: String,
        bookDescription: String,
        bookDate: String,
        bookImage: Int?,
        bookReviews: String?,
        bookProgress: Float,
        bookGenre: String
    )
}
