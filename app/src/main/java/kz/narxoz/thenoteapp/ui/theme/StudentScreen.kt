package kz.narxoz.thenoteapp.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Icon
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.narxoz.thenoteapp.database.Student
import kz.narxoz.thenoteapp.viewmodels.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(viewModel: StudentViewModel = viewModel()) {
    val students by viewModel.allStudents.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список студентов", color = Color.White) },
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
                    items(students) { student ->
                        StudentCard(
                            student,
                            viewModel,
                            coroutineScope,
                            onEditClick = {
                                selectedStudent = student
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
                    Icon(Icons.Filled.Add, contentDescription = "Добавить студента")
                }
            }

            if (showEditDialog) {
                EditStudentDialog(
                    student = selectedStudent,
                    onDismiss = { showEditDialog = false },
                    onConfirm = { updatedStudent ->
                        coroutineScope.launch {
                            if (updatedStudent.id == 0) {
                                viewModel.insertStudent(updatedStudent)
                            } else {
                                viewModel.updateStudent(
                                    updatedStudent.id,
                                    updatedStudent.studentName,
                                    updatedStudent.studentPhone
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
fun StudentCard(
    student: Student,
    viewModel: StudentViewModel,
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
                    text = "Имя: ${student.studentName}",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
                Text(
                    text = "Телефон: ${student.studentPhone}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteStudent(student.id)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить студента",
                    tint = Color(0xFFFF3B30) // Красный цвет для удаления
                )
            }
            IconButton(
                onClick = { onEditClick() } // Вызов onEditClick при нажатии на кнопку "Изменить"
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать студента",
                    tint = Color(0xFF9B4DFF) // Фиолетовый цвет для редактирования
                )
            }
        }
    }
}

@Composable
fun EditStudentDialog(
    student: Student? = null,
    onDismiss: () -> Unit,
    onConfirm: (Student) -> Unit
) {
    var name by remember { mutableStateOf(student?.studentName ?: "") }
    var phone by remember { mutableStateOf(student?.studentPhone?.toString() ?: "") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(if (student == null) "Добавить студента" else "Редактировать студента") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && name.isBlank(),
                    placeholder = { Text("Введите имя") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { char -> char.isDigit() } },
                    label = { Text("Телефон") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError && phone.isBlank(),
                    placeholder = { Text("Введите номер телефона") }
                )
                if (isError) {
                    Text(
                        text = "Пожалуйста, заполните все поля корректно.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val studentPhone = phone.toLongOrNull()
                if (name.isNotBlank() && studentPhone != null && studentPhone > 0) {
                    onConfirm(
                        student?.copy(studentName = name, studentPhone = studentPhone)
                            ?: Student(studentName = name, studentPhone = studentPhone)
                    )
                } else {
                    isError = true
                }
            }) {
                Text(if (student == null) "Добавить" else "Обновить")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Отмена")
            }
        }
    )
}




@Composable
fun StudentList(
    students: List<Student>,
    onAddStudent: () -> Unit,
    onDeleteStudent: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(students) { student ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Имя: ${student.studentName}")
                    Text(text = "Телефон: ${student.studentPhone}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onDeleteStudent(student.id) }) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onAddStudent) {
        Text("Добавить студента")
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onAdd: (Student) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Добавить студента") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && name.isBlank(),
                    placeholder = { Text("Введите имя") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { char -> char.isDigit() } }, // Удаляем лишние символы
                    label = { Text("Телефон") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError && phone.isBlank(),
                    placeholder = { Text("Введите номер телефона") }
                )
                if (isError) {
                    Text(
                        text = "Пожалуйста, заполните все поля корректно.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val studentPhone = phone.toLongOrNull() // Используем Long вместо Int
                if (name.isNotBlank() && studentPhone != null && studentPhone > 0) {
                    onAdd(Student(studentName = name, studentPhone = studentPhone))
                    onDismiss()
                } else {
                    isError = true
                }
            }) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Отмена")
            }
        }
    )
}

