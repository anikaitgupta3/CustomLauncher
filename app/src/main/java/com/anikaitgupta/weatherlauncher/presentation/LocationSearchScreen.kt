package com.anikaitgupta.weatherlauncher.presentation

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anikaitgupta.weatherlauncher.R


@Composable
fun LocationSearchScreen(
    modifier: Modifier,
    onCityEntered: (String) -> Unit,
    enteredCity: String,
    onButtonClick: () -> Unit,
    textToShowInTextView: String,
    showProgress: Boolean,
    onDraggedLeft: () -> Unit,
    isLauncherDefault: Boolean
) {
    //val enteredCity by viewModel._enteredCity.collectAsState()
    var offsetX by remember { mutableStateOf(0f) }
    val threshold = -200f // Threshold in pixels to consider "dragged right"
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Light blue background from image
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    // Update offset, restricted to rightward only if desired
                    offsetX += delta
                },
                onDragStopped = {
                    // Action: Check if dragged far enough right
                    if (offsetX < threshold) {
                        onDraggedLeft()
                    }
                    // Optionally snap back or reset
                    offsetX = 0f
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))


            
            // Image Placeholder
            Image(
                painter = painterResource(R.drawable.img), // Placeholder
                contentDescription = "Weather Icon",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Enter location",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = enteredCity,
                onValueChange = { onCityEntered(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = textToShowInTextView, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            if(showProgress) {
                CircularProgressIndicator()
            }


        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03A9F4) // Blue from image
                )
            ) {
                Text(
                    text = "Search weather",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (!isLauncherDefault) {
                TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                    context.startActivity(intent)
                }) {
                    Text(text = "Set as default launcher", color = Color(0xFF03A9F4))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .clickable {
                        uriHandler.openUri("https://sites.google.com/view/weatherlauncherprivacypolicy/home")
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Privacy Policy",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Privacy Policy",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}
//@Preview
//@Composable
//fun LocationSearchScreenPreview() {
//    LocationSearchScreen(
//        modifier,
//        onCityEntered = {},
//        enteredCity = "",
//        onButtonClick = {},
//        "Enter a city",
//        false
//    ) {}
//}
