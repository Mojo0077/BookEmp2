// 🔧 CameraGalleryActivity.kt
package com.example.bookemp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.bookemp.ui.theme.BookempTheme
import coil.compose.rememberAsyncImagePainter

class CameraGalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookempTheme {
                CameraGalleryScreen(onBack = { finish() })
            }
        }
    }
}

@Composable
fun CameraGalleryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val customFont = FontFamily(Font(R.font.cat_childs))

    var cameraImage by remember { mutableStateOf<Bitmap?>(null) }
    var galleryImage by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            cameraImage = imageBitmap
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        galleryImage = uri
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("📷 Kamera & Galéria", fontSize = 28.sp, fontFamily = customFont, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                } else {
                    // TODO: Kamera jogosultság kérése külön activity-ben vagy runtime kezeléssel
                }
            }) {
                Text("📸 Fotó készítése")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Text("🖼️ Kép kiválasztása galériából")
            }

            Spacer(modifier = Modifier.height(24.dp))

            cameraImage?.let {
                Text("Kamera kép:", fontFamily = customFont, color = Color.White)
                Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.height(150.dp), contentScale = ContentScale.Crop)
            }

            galleryImage?.let {
                Text("Galéria kép:", fontFamily = customFont, color = Color.White, modifier = Modifier.padding(top = 16.dp))
                Image(painter = rememberAsyncImagePainter(it), contentDescription = null, modifier = Modifier.height(150.dp), contentScale = ContentScale.Crop)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onBack) {
                Text("⬅️ Vissza")
            }
        }
    }
}


