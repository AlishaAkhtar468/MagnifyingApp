package com.example.magnifyingapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magnifyingapplication.R
import com.example.magnifyingapplication.ui.theme.BodyTextStyle
import com.example.magnifyingapplication.ui.theme.PrimaryColor
import com.example.magnifyingapplication.ui.theme.DarkBackground
import com.example.magnifyingapplication.ui.theme.LightBackground
import com.example.magnifyingapplication.ui.theme.headingTextStyle
import com.example.magnifyingapplication.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    onGetStarted: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val isDark by viewModel.isDarkTheme.collectAsState()

    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val textColor = PrimaryColor

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        IconButton(
            onClick = { viewModel.toggleTheme() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = "Toggle Theme",
                modifier = Modifier.size(70.dp),
                tint = textColor
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.magnifier),
                    contentDescription = "Grid Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.bg_grid),
                    contentDescription = "Magnifier",
                    modifier = Modifier.size(320.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "More rewarding travel",
                style = headingTextStyle(),

            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Find and compare offers from multiple\nsuppliers at once",

                style = BodyTextStyle(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text("Get Started", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
