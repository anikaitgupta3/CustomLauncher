package com.example.customlauncher

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppItem(appBlock: AppBlock,onClick:()-> Unit){
    Column{
        Image(painter = rememberDrawablePainter(appBlock.icon), contentDescription = appBlock.appName,
            Modifier.clickable(){
                onClick()
            }.size(50.dp))
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = appBlock.appName)
    }
}