package com.example.bookemp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.bookemp.ui.theme.BookempTheme

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookempTheme {
                AboutScreen()
            }
        }
    }
}

@Composable
fun AboutScreen() {
    val customFont = FontFamily(Font(R.font.cat_childs))
    val context = LocalContext.current

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
            Text("📚 BookEmp alkalmazás", fontSize = 28.sp, fontFamily = customFont, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Verzió: 1.0\nKészítette: Jónás Mónika", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { (context as? ComponentActivity)?.finish() }) {
                Text("⬅️ Vissza")
            }
        }
    }
}

