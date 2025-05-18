// ✅ Végleges stabil EditBookScreen – kameraengedéllyel és MediaStore mentéssel
package com.example.bookemp.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookemp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun createImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "book_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BookEmp")
        }
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

@Composable
fun EditBookScreen(
    libraryId: String,
    bookId: String,
    currentTitle: String,
    currentAuthor: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val customFont = FontFamily(Font(R.font.cat_childs))

    var title by remember { mutableStateOf(currentTitle) }
    var author by remember { mutableStateOf(currentAuthor) }
    var imageFileUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (!success) {
            Toast.makeText(context, "Nem sikerült a fénykép készítése", Toast.LENGTH_SHORT).show()
            imageFileUri = null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            if (uri != null) {
                imageFileUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Nem sikerült létrehozni a képfájlt", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Kameraengedély megtagadva", Toast.LENGTH_SHORT).show()
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
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✏️ Könyv szerkesztése",
                fontSize = 28.sp,
                fontFamily = customFont,
                color = Color(0xFFDFB148),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Cím", color = Color(0xFFDFB148)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33DFB148),
                    unfocusedContainerColor = Color(0x33DFB148)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Szerző", color = Color(0xFFDFB148)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33DFB148),
                    unfocusedContainerColor = Color(0x33DFB148)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        val uri = createImageUri(context)
                        if (uri != null) {
                            imageFileUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            Toast.makeText(context, "Nem sikerült létrehozni a képfájlt", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📸 Borítókép készítése", fontFamily = customFont, color = Color(0xFFDFB148))
            }

            imageFileUri?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (userId != null && title.isNotBlank() && author.isNotBlank()) {
                        val dataToUpdate = mutableMapOf<String, Any>(
                            "title" to title,
                            "author" to author
                        )
                        imageFileUri?.let {
                            dataToUpdate["coverImageUri"] = it.toString()
                        }
                        db.collection("users")
                            .document(userId)
                            .collection("libraries")
                            .document(libraryId)
                            .collection("books")
                            .document(bookId)
                            .update(dataToUpdate)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Könyv frissítve!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Hiba: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💾 Mentés", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C1B00)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⬅️ Mégsem", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }
        }
    }
}
