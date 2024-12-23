package kz.narxoz.thenoteapp.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.narxoz.thenoteapp.database.BookDatabase

import kz.narxoz.thenoteapp.database.Book
import kz.narxoz.thenoteapp.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(viewModel: BookViewModel = viewModel()) {
    // Состояние для списка книг, которое будет обновляться при изменении данных в Flow
    val books by viewModel.allBooks.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список книг", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9B4DFF)
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(books) { index, book ->
                        BookCard(
                            book,
                            viewModel,
                            coroutineScope,
                            onEditClick = {
                                selectedBook = book
                                showEditDialog = true
                            }
                        )
                    }
                }


                FloatingActionButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    containerColor = Color(0xFF9B4DFF),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Добавить книгу")
                }
            }

            if (showEditDialog) {
                EditBookDialog(
                    book = selectedBook,
                    onDismiss = { showEditDialog = false },
                    onConfirm = { updatedBook ->
                        coroutineScope.launch {
                            if (updatedBook.id == 0) {
                                viewModel.insertBook(updatedBook)
                            } else {
                                viewModel.updateBook(
                                    updatedBook.id,
                                    updatedBook.title,
                                    updatedBook.author,
                                    updatedBook.description,
                                    updatedBook.date,
                                    updatedBook.image,
                                    updatedBook.reviews,
                                    updatedBook.progress,
                                    updatedBook.genre
                                )
                            }
                        }
                        showEditDialog = false
                    }
                )
            }
        }
    )
}

@Composable
fun BookCard(
    book: Book,
    viewModel: BookViewModel,
    coroutineScope: CoroutineScope,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Название: ${book.title}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Автор: ${book.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteBook(book.id)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить книгу",
                    tint = Color(0xFFFF3B30)
                )
            }

            IconButton(
                onClick = { onEditClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать книгу",
                    tint = Color(0xFF9B4DFF)
                )
            }
        }
    }
}

@Composable
fun EditBookDialog(
    book: Book? = null,
    onDismiss: () -> Unit,
    onConfirm: (Book) -> Unit
) {
    var title by remember { mutableStateOf(book?.title ?: "") }
    var author by remember { mutableStateOf(book?.author ?: "") }
    var description by remember { mutableStateOf(book?.description ?: "") }
    var date by remember { mutableStateOf(book?.date ?: "") }
    var progress by remember { mutableStateOf(book?.progress ?: 0f) }
    var genre by remember { mutableStateOf(book?.genre ?: "") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(if (book == null) "Добавить книгу" else "Редактировать книгу") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && title.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Автор") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && author.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && description.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Дата") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && date.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Жанр") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && genre.isBlank()
                )
                if (isError) {
                    Text(
                        text = "Пожалуйста, заполните все поля корректно.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && author.isNotBlank() && description.isNotBlank() && date.isNotBlank() && genre.isNotBlank()) {
                    onConfirm(
                        book?.copy(title = title, author = author, description = description, date = date, progress = progress, genre = genre)
                            ?: Book(title = title, author = author, description = description, date = date, progress = progress, genre = genre)
                    )
                } else {
                    isError = true
                }
            }) {
                Text(if (book == null) "Добавить" else "Обновить")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Отмена")
            }
        }
    )
}
