// ✅ Teljesen javított BookListScreen.kt
package com.example.bookemp.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookemp.R
import com.example.bookemp.models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookListScreen(
    libraryId: String,
    libraryName: String,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val books = remember { mutableStateListOf<Book>() }
    var sortOption by remember { mutableStateOf("Alap") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedBookToDelete by remember { mutableStateOf<Book?>(null) }
    val customFont = FontFamily(Font(R.font.cat_childs))

    LaunchedEffect(userId, libraryId, sortOption) {
        if (userId != null) {
            val query = db.collection("users")
                .document(userId)
                .collection("libraries")
                .document(libraryId)
                .collection("books")

            val sortedQuery = when (sortOption) {
                "Szerző" -> query.orderBy("author")
                "Cím" -> query.orderBy("title")
                "Legfrissebb" -> query.orderBy("year", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(5)
                else -> query
            }

            sortedQuery.get()
                .addOnSuccessListener { result ->
                    books.clear()
                    result.forEach { doc ->
                        val book = doc.toObject(Book::class.java).copy(id = doc.id)
                        books.add(book)
                    }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Hiba: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.kezdo_oldal),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📖 Könyvek listája", fontSize = 28.sp, fontFamily = customFont, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Könyvtár: $libraryName", fontSize = 16.sp, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("Alap", "Szerző", "Cím", "Legfrissebb").forEach { option ->
                    Button(
                        onClick = { sortOption = option },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
                    ) {
                        Text(option, color = Color(0xFFDFB148))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(books) { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("book", "${book.author} - ${book.title}")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Kimásolva: ${book.author} - ${book.title}", Toast.LENGTH_SHORT).show()
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C2C1F))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            book.coverImageUri?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(book.title, fontSize = 20.sp, color = Color(0xFFDFB148))
                                    Text("Szerző: ${book.author}", fontSize = 16.sp, color = Color.White)
                                }
                                Row {
                                    IconButton(onClick = {
                                        selectedBookToDelete = book
                                        showConfirmDialog = true
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "Törlés",
                                            tint = Color.Gray
                                        )
                                    }
                                    IconButton(onClick = {
                                        navController?.navigate("editBook/$libraryId/${book.id}/${book.title}/${book.author}")
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_edit),
                                            contentDescription = "Szerkesztés",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController?.navigate("createBook/$libraryId")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("➕ Új könyv hozzáadása", color = Color(0xFFDFB148))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController?.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("⬅️ Vissza", color = Color.White)
            }
        }

        if (showConfirmDialog && selectedBookToDelete != null) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Biztosan törlöd a könyvet?") },
                text = { Text("A művelet nem visszavonható.") },
                confirmButton = {
                    TextButton(onClick = {
                        val book = selectedBookToDelete
                        if (userId != null && book != null) {
                            db.collection("users")
                                .document(userId)
                                .collection("libraries")
                                .document(libraryId)
                                .collection("books")
                                .document(book.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Könyv törölve!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Hiba: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        showConfirmDialog = false
                        selectedBookToDelete = null
                    }) {
                        Text("Igen")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showConfirmDialog = false
                        selectedBookToDelete = null
                    }) {
                        Text("Mégsem")
                    }
                }
            )
        }
    }
}
