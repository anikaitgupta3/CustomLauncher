package com.example.customlauncher

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun BottomSheetContent(appList: MutableList<AppBlock>,onClick:(String)-> Unit, onAppInfoClick:(String)->Unit){
    var text by remember { mutableStateOf("")}
    val filteredList by remember(text, appList) {
        derivedStateOf {
            if (text.isEmpty()) {
                appList
            } else {
                appList.filter { it.appName.contains(text, ignoreCase = true) }
            }
        }
    }
    Column {
        OutlinedTextField(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            label = { Text("Search an app") }
        )
        Spacer(Modifier.size(10.dp))
        LazyColumn(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(filteredList) { appItem ->
                BottomSheetItem(
                    appItem, { onClick(appItem.packageName) },
                    { onAppInfoClick(appItem.packageName) })

            }
        }
    }
}
@Composable
fun BottomSheetItem(appItem: AppBlock, onClick:()-> Unit,onAppInfoClick:()->Unit){
    var expanded by remember { mutableStateOf(false) }
    Box() {
        Row(
            Modifier.combinedClickable(
                onClick = onClick,
                onLongClick = { expanded = !expanded }
            )) {
            Image(
                painter = rememberDrawablePainter(appItem.icon),
                contentDescription = appItem.appName,
                Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = appItem.appName, fontSize = 15.sp)
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