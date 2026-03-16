package com.example.customlauncher

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(appBlock: AppBlock,onClick:()-> Unit,onAppInfoClick:()->Unit){
    var expanded by remember { mutableStateOf(false) }
    Box() {
        Column(
            Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = { expanded = !expanded }
        )) {
            Image(
                painter = rememberDrawablePainter(appBlock.icon),
                contentDescription = appBlock.appName,
                Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = appBlock.appName, fontSize = 15.sp)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("App Info") },
                onClick = { expanded = false
                    onAppInfoClick() }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Delete app") },
                onClick = { /* Do something... */ }
            )
        }
    }
}