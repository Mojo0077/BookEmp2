// ✅ Teljesen javított LibraryListScreen.kt
package com.example.bookemp.ui.screens

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
import com.example.bookemp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LibraryListScreen(
    navController: NavHostController? = null,
    onBack: () -> Unit = {},
    onLibrarySelected: (libraryId: String, libraryName: String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val libraries = remember { mutableStateListOf<Pair<String, String>>() } // Pair<id, name>
    val customFont = FontFamily(Font(R.font.cat_childs))

    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("libraries")
                .get()
                .addOnSuccessListener { result ->
                    libraries.clear()
                    result.forEach { doc ->
                        val name = doc.getString("name") ?: "Ismeretlen"
                        libraries.add(doc.id to name)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Hiba: ${it.message}", Toast.LENGTH_SHORT).show()
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
            Text(
                text = "📁 Könyvtárak listája",
                fontSize = 28.sp,
                fontFamily = customFont,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(libraries) { (id, name) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                if (navController != null) {
                                    navController.navigate("bookList/$id/$name")
                                } else {
                                    onLibrarySelected(id, name)
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C2C1F))
                    ) {
                        Text(
                            text = name,
                            fontSize = 20.sp,
                            color = Color(0xFFDFB148),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onBack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("⬅️ Vissza", color = Color.White, fontFamily = customFont)
            }
        }
    }
}
