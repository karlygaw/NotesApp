package kz.narxoz.thenoteapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kz.narxoz.thenoteapp.database.Book
import kz.narxoz.thenoteapp.database.BookDao

class BookViewModel(private val bookDao: BookDao) : ViewModel() {
    // Получить все книги
    val allBooks: Flow<List<Book>> = bookDao.getAll()

    // Вставить новую книгу
    fun insertBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookDao.addData(book)
            } catch (e: Exception) {
                println("Error inserting book: ${e.message}")
            }
        }
    }

    // Удалить книгу по ID
    fun deleteBook(bookId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookDao.deleteById(bookId)
            } catch (e: Exception) {
                println("Error deleting book: ${e.message}")
            }
        }
    }

    // Обновить книгу по ID
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
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookDao.updateBook(
                    bookId,
                    bookTitle,
                    bookAuthor,
                    bookDescription,
                    bookDate,
                    bookImage,
                    bookReviews,
                    bookProgress,
                    bookGenre
                )
            } catch (e: Exception) {
                println("Error updating book: ${e.message}")
            }
        }
    }
}
