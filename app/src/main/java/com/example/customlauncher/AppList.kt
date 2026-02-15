package com.example.customlauncher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat

@Composable
fun AppList(appList: MutableList<AppBlock>,onClick:(String)-> Unit){
    LazyVerticalGrid(columns = GridCells.Fixed(3), Modifier.padding(10.dp), verticalArrangement = Arrangement.SpaceEvenly, horizontalArrangement = Arrangement.SpaceEvenly) {
        items(appList){ appItem->
            AppItem(appItem){
                onClick(appItem.packageName)
            }
        }
    }
}