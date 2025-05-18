package com.example.bookemp.ui.screens

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookemp.*
import com.example.bookemp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainMenuScreen(navController: NavHostController) {
    val customFont = FontFamily(Font(R.font.cat_childs))
    val context = LocalContext.current
    val scale = remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(targetValue = scale.value, label = "scale")
    val coroutineScope = rememberCoroutineScope()

    val colorList = listOf(
        Color(0xFFDFB148),
        Color(0xFFFF9800),
        Color(0xFFE91E63)
    )
    var index by remember { mutableStateOf(0) }
    val color by animateColorAsState(targetValue = colorList[index], label = "color")

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            index = (index + 1) % colorList.size
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.kezdo_oldal),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BookEmp",
                fontSize = 48.sp,
                fontFamily = customFont,
                color = color,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    scale.value = 1.2f
                    coroutineScope.launch {
                        delay(150)
                        scale.value = 1f
                        delay(100)
                        navController.navigate("libraryList")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("📁 Könyvtáram (Compose)", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = { navController.navigate("createLibrary") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("➕ Új könyvtár (Compose)", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("mainMenu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("🚪 Kilépés", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, AboutActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("ℹ️ Névjegy (Activity)", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, LibraryListActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("📚 Könyvtárlista (Activity)", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = {
                    val intent = Intent(context, BookListActivity::class.java)
                    intent.putExtra("libraryId", "teszt")
                    intent.putExtra("libraryName", "Teszt könyvtár")
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("📖 Könyvlista (Activity)", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, WelcomeActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("👋 Üdvözlő oldal (Activity 4)", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, CameraGalleryActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C2C1F))
            ) {
                Text("📷 Kamera & Galéria", fontFamily = customFont, fontSize = 20.sp, color = Color(0xFFDFB148))
            }
        }
    }
}
