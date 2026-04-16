package com.anikaitgupta.weatherlauncher.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(appBlock: AppBlock, onClick:()-> Unit, onAppInfoClick:()->Unit, updateDraggingAppPackage:()->Unit,
            updateDraggingAppPackageToNull: () -> Unit,onRemoveFromFavorite:()-> Unit,flag:Int){
    //var initialTouchInWindow by remember { mutableStateOf(Offset.Zero) }
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
        )
    }.pointerInput(Unit){
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                //val windowOffset = it.boundsInWindow().topLeft

                // 2. initialTouchInWindow is now a "Global" point (e.g., x=500, y=1200)
                //initialTouchInWindow = windowOffset + offset
                updateDraggingAppPackage()
                isDragging=true
                //disablePagerScroll()
                expanded=false

            },
            onDrag = { change, dragAmount ->
                change.consume()
                // 3. Update dragOffset += dragAmount
                dragOffset+=dragAmount
                // 3. CRITICAL: Calculate where the finger is on the WHOLE screen
                //val currentFingerPos = initialTouchInWindow + dragOffset

                // 4. Send this to the Parent (MainAppContent) to check for collisions
                //onDragAction(currentFingerPos)
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
                    //onDropAction()
                }
                //enablePagerScroll()
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
                //enablePagerScroll()
                updateDraggingAppPackageToNull()
            }
        )
    }) {
        val context = LocalContext.current
        // Dynamically load the icon using the package name stored in DB
        val icon = remember(appBlock.packageName) {
            try {
                context.packageManager.getApplicationIcon(appBlock.packageName)
            } catch (e: Exception) {
                null // Fallback icon here
            }
        }

        Column {
            icon?.let {
                //Column() {
                Image(
                    painter = rememberDrawablePainter(it),
                    contentDescription = appBlock.appName,
                    Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.size(5.dp))
            //Text(text = appBlock.appName, fontSize = 15.sp)
            Text(
                text = appBlock.appName,
                color = Color.White,
                fontSize = 14.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
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
            if(flag==0) {
                DropdownMenuItem(
                    text = { Text("Remove from home screen") },
                    onClick = { onRemoveFromFavorite() }
                )
            }
        }
    }
}