package com.anikaitgupta.weatherlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anikaitgupta.weatherlauncher.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppList(
    appList: List<AppBlock>,
    favoriteAppsList: List<AppBlock>,
    hotSeatAppList: List<AppBlock>,
    modifier: Modifier,
    onClick: (String) -> Unit,
    onAppInfoClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    onAddToFavorite:(AppBlock)-> Unit,
    onRemoveFromFavorite:(AppBlock)-> Unit,
    onDraggedRight: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    //val scope = rememberCoroutineScope()
    //val pagerState = rememberPagerState(pageCount = { 2 })
    var showBottomSheet by remember { mutableStateOf(false) }
    var draggingAppPackage by remember { mutableStateOf<String?>(null) }
    //var isPagerScrollEnabled by remember { mutableStateOf(true) }
    MainAppContent(
        favoriteAppsList,
        hotSeatAppList,
        modifier,
        onClick,
        onAppInfoClick,
        //pagerState,
        { showBottomSheet = true },
        { onSearch(it) },
        { draggingAppPackage = it },
        { draggingAppPackage = null },
        onRemoveFromFavorite,
        onDraggedRight,
        //{ isPagerScrollEnabled = false },
        //{ isPagerScrollEnabled = true },
        //isPagerScrollEnabled,
        )
    if (showBottomSheet) {
        BottomSheetView(appList, onClick, onAppInfoClick, sheetState,{
            showBottomSheet = false
        },{onAddToFavorite(it)})
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    favoriteAppsList: List<AppBlock>,
    hotSeatAppList: List<AppBlock>,
    modifier: Modifier,
    onClick: (String) -> Unit,
    onAppInfoClick: (String) -> Unit,
    onSwipeUp: () -> Unit,
    onSearch: (String) -> Unit,
    updateDraggingAppPackage: (String) -> Unit,
    updateDraggingAppPackageToNull: () -> Unit,
    onRemoveFromFavorite: (AppBlock) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    // If dragAmount is negative and significant (e.g., < -20)
                    if (dragAmount < -20) {
                        onSwipeUp()
                    }
                }
            },
    ) {
        Column() {
            var text by remember { mutableStateOf("") }
            Surface(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f)) { }
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(favoriteAppsList) { appItem ->
                    AppItem(
                        appItem,
                        { onClick(appItem.packageName) },
                        { onAppInfoClick(appItem.packageName) },
                        { updateDraggingAppPackage(appItem.packageName) },
                        { updateDraggingAppPackageToNull() },
                        {onRemoveFromFavorite(appItem)},
                        0
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                shape = RoundedCornerShape(50),
                placeholder = { Text("Search something on web") },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = "Search",
                        modifier = Modifier.clickable() {
                            onSearch(text)
                        }
                    )
                }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (appItem in hotSeatAppList) {
                    AppItem(
                        appItem,
                        { onClick(appItem.packageName) },
                        { onAppInfoClick(appItem.packageName) },
                        { updateDraggingAppPackage(appItem.packageName) },
                        { updateDraggingAppPackageToNull() },{onRemoveFromFavorite(appItem)},1
                    )
                }
            }
        }
    }
}*/
@Composable
fun MainAppContent(
    favoriteAppsList: List<AppBlock>,
    hotSeatAppList: List<AppBlock>,
    modifier: Modifier,
    onClick: (String) -> Unit,
    onAppInfoClick: (String) -> Unit,
    onSwipeUp: () -> Unit,
    onSearch: (String) -> Unit,
    updateDraggingAppPackage: (String) -> Unit,
    updateDraggingAppPackageToNull: () -> Unit,
    onRemoveFromFavorite: (AppBlock) -> Unit,
    onDraggedRight:()-> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val threshold = 200f // Threshold in pixels to consider "dragged right"
    Surface(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    // If dragAmount is negative and significant (e.g., < -20)
                    if (dragAmount < -20) {
                        onSwipeUp()
                    }
                }
            }.draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    // Update offset, restricted to rightward only if desired
                    offsetX += delta
                },
                onDragStopped = {
                    // Action: Check if dragged far enough right
                    if (offsetX > threshold) {
                        onDraggedRight()
                    }
                    // Optionally snap back or reset
                    offsetX = 0f
                }
            ),
        color = Color.Transparent, // Explicitly set Surface to transparent
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // --- 1. DATE & TIME CONTAINER ---
                DateTimeContainer()

                // --- 2. FAVORITE APPS (Starting from center-ish) ---
                // We give this a weight so it pushes the search bar down
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        // Adding top padding here determines how far down the icons start
                        contentPadding = PaddingValues(
                            top = 100.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favoriteAppsList) { appItem ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AppItem(
                                    appItem,
                                    { onClick(appItem.packageName) },
                                    { onAppInfoClick(appItem.packageName) },
                                    { updateDraggingAppPackage(appItem.packageName) },
                                    { updateDraggingAppPackageToNull() },
                                    { onRemoveFromFavorite(appItem) },
                                    0
                                )
                            }
                        }
                    }
                }

                // --- 3. SEARCH & HOTSEAT (Pinned to Bottom) ---
                BottomSection(
                    onSearch,
                    hotSeatAppList,
                    onClick,
                    onAppInfoClick,
                    updateDraggingAppPackage,
                    updateDraggingAppPackageToNull,
                    onRemoveFromFavorite
                )
            }
        }
    }
}

@Composable
fun DateTimeContainer() {
    // Basic Date/Time Logic
    val calendar = Calendar.getInstance()
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())

    val time = timeFormat.format(calendar.time)
    val date = dateFormat.format(calendar.time)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            fontSize = 64.sp,
            color = Color.White,
            fontWeight = FontWeight.Light,
            style = TextStyle(shadow = Shadow(Color.Black, Offset(2f, 2f), 4f))
        )
        Text(
            text = date,
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.9f),
            style = TextStyle(shadow = Shadow(Color.Black, Offset(1f, 1f), 2f))
        )
    }
}

@Composable
fun BottomSection(
    onSearch: (String) -> Unit,
    hotSeatAppList: List<AppBlock>,
    onClick: (String) -> Unit,
    onAppInfoClick: (String) -> Unit,
    updateDraggingAppPackage: (String) -> Unit,
    updateDraggingAppPackageToNull: () -> Unit,
    onRemoveFromFavorite: (AppBlock) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text by remember { mutableStateOf("") }

        // Search Bar
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50),
            placeholder = { Text("Search Web", color = Color.White.copy(0.6f)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Black.copy(0.25f),
                unfocusedContainerColor = Color.Black.copy(0.25f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = { onSearch(text) }) {
                    Icon(painterResource(R.drawable.baseline_search_24), "Search", tint = Color.White)
                }
            }
        )

        // Hotseat - Forced to 4 columns for symmetry
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (appItem in hotSeatAppList) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    AppItem(
                        appItem,
                        { onClick(appItem.packageName) },
                        { onAppInfoClick(appItem.packageName) },
                        { updateDraggingAppPackage(appItem.packageName) },
                        { updateDraggingAppPackageToNull() },
                        { onRemoveFromFavorite(appItem) },
                        1
                    )
                }
            }
        }
    }
}

