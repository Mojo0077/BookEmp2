package com.example.bookemp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookemp.R
import com.example.bookemp.models.Library
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun CreateLibraryScreen(
    onLibraryCreated: () -> Unit
) {
    val customFont = FontFamily(Font(R.font.cat_childs))
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var libraryName by remember { mutableStateOf("") }

    // 🔧 EZ A KULCS
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Image(
            painter = painterResource(id = R.drawable.kezdo_oldal),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize() // fontosság!
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📚 Új könyvtár",
                fontSize = 32.sp,
                fontFamily = customFont,
                color = Color(0xFFDFB148),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = libraryName,
                onValueChange = { libraryName = it },
                label = { Text("Könyvtár neve", color = Color(0xFFDFB148)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33DFB148),
                    unfocusedContainerColor = Color(0x33DFB148)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (libraryName.isNotBlank() && userId != null) {
                        val newLibrary = Library(
                            name = libraryName,
                            userId = userId,
                            createdAt = Date()
                        )

                        db.collection("users")
                            .document(userId)
                            .collection("libraries")
                            .add(newLibrary)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Könyvtár létrehozva!", Toast.LENGTH_SHORT).show()
                                onLibraryCreated()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Hiba történt: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Add meg a könyvtár nevét!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("Létrehozás", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }
        }
    }
}
