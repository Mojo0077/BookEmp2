package com.example.bookemp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookemp.models.Book
import com.example.bookemp.ui.theme.BookempTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookempTheme {
                BookListScreen(onBack = { finish() })
            }
        }
    }
}

@Composable
fun BookListScreen(onBack: () -> Unit) {
    val customFont = FontFamily(Font(R.font.cat_childs))
    val books = remember { mutableStateListOf<Book>() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(it)
                .collection("libraries")
                .get()
                .addOnSuccessListener { libraries ->
                    libraries.forEach { lib ->
                        lib.reference.collection("books").get()
                            .addOnSuccessListener { result ->
                                result.forEach { doc ->
                                    val book = doc.toObject(Book::class.java)
                                    books.add(book)
                                }
                            }
                    }
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
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
        ) {
            Text("📚 Könyvek", fontFamily = customFont, fontSize = 28.sp, color = Color.White)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(books) { book ->
                    Text("${book.title} - ${book.author}", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text("⬅️ Vissza")
            }
        }

    }
}

