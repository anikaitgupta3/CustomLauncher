package com.example.customlauncher

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(appBlock: AppBlock,onClick:()-> Unit,onAppInfoClick:()->Unit,updateDraggingAppPackage:()->Unit,
            updateDraggingAppPackageToNull: () -> Unit,disablePagerScroll:()-> Unit,
            enablePagerScroll:()-> Unit,){
    var expanded by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    Box(Modifier.graphicsLayer {
        translationX = dragOffset.x
        translationY = dragOffset.y
        scaleX = if (isDragging) 1.1f else 1f
        scaleY = if (isDragging) 1.1f else 1f
    }.pointerInput(Unit) {
        // Handle simple Taps (Click to launch)
        detectTapGestures(
            onTap = { onClick() },
            /*onLongPress = {
                // This shows the menu if the user just holds and releases
                expanded = !expanded
            }*/
        )
    }.pointerInput(Unit){
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                // 1. Identify which app is being touched
                // 2. Set draggingAppPackage
                updateDraggingAppPackage()
                isDragging=true
                disablePagerScroll()
                expanded=false

            },
            onDrag = { change, dragAmount ->
                change.consume()
                // 3. Update dragOffset += dragAmount
                dragOffset+=dragAmount
                // If they move significantly, ensure menu stays closed
                if (dragOffset.getDistance() > 10f) {
                    expanded = false
                }
            },
            onDragEnd = {
                // 4. Reset states
                //dragOffset=Offset.Zero
                //isDragging=false
                //updateDraggingAppPackageToNull()
                //enablePagerScroll()// Logic: If the user long-pressed but didn't really move (< 10 pixels),
                // they probably wanted the menu, not a drag.
                if (dragOffset.getDistance() < 10f) {
                    expanded = true
                    isDragging = false // Cancel the "lifted" look
                } else {
                    // Here is where the "Drop" logic will go!
                    isDragging = false
                    dragOffset = Offset.Zero
                }
                enablePagerScroll()
                updateDraggingAppPackageToNull()
            },
            onDragCancel = {
                // Logic: If the user long-pressed but didn't really move (< 10 pixels),
                // they probably wanted the menu, not a drag.
                if (dragOffset.getDistance() < 10f) {
                    expanded = true
                    isDragging = false // Cancel the "lifted" look
                } else {
                    // Here is where the "Drop" logic will go!
                    isDragging = false
                    dragOffset = Offset.Zero
                }
                enablePagerScroll()
                updateDraggingAppPackageToNull()
            }
        )
    }) {
        Column() {
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