package com.example.magnifyingapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.magnifyingapplication.ui.theme.MagnifyingApplicationTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagnifyingApplicationTheme {
                rememberNavController()
                SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            // Bigger crown rotated at top right
            Image(
                painter = painterResource(id = R.drawable.crown),
                contentDescription = "Crown",
                modifier = Modifier
                    .size(130.dp) // increased size
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .rotate(20f)
            )

            // Bigger red circle bottom right
            Box(
                modifier = Modifier
                    .size(70.dp) // increased size
                    .align(Alignment.BottomEnd)
                    .offset(x = 30.dp, y = 30.dp)
                    .background(Color(0xFFFF2D55), shape = CircleShape)
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Magnifier Pro",
                    color = Color.Black,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
                ProFeatureRow("Unlimited saves")
                ProFeatureRow("Multiple results")
                ProFeatureRow("No ads")

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(42.dp)
                ) {
                    Text("Try Pro Now", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionCard(title = "Social") {
            SettingRow("Share App")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionCard(title = "Help") {
            SettingRow("Help Center", Icons.Default.Help)
            SettingRow("Contact Support", Icons.Default.Link)
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionCard(title = "Legal") {
            SettingRow("Terms of Service", Icons.Default.Policy)
            SettingRow("Privacy Policy", Icons.Default.Policy)
            SettingRow("Privacy Setting", Icons.Default.PrivacyTip)
            SettingRow("Privacy Preferences", Icons.Default.Settings)
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun SettingRow(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color.White)
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun ProFeatureRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFFFF2D55),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.Black, fontSize = 14.sp)
    }
}
