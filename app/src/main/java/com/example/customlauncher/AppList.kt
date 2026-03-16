package com.example.customlauncher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppList(appList: MutableList<AppBlock>, appListNew: MutableList<List<AppBlock>>, hotSeatAppList: MutableList<AppBlock>, modifier: Modifier, onClick:(String)-> Unit, onAppInfoClick:(String)->Unit,onSearch: (String) -> Unit){
    val sheetState = rememberModalBottomSheetState()
    //val scope = rememberCoroutineScope()
    val pagerState =rememberPagerState(pageCount = { 2 })
    var showBottomSheet by remember { mutableStateOf(false) }
    //val textFieldState = rememberTextFieldState()
    MainAppContent(appListNew,hotSeatAppList,modifier,onClick,onAppInfoClick,pagerState,{ showBottomSheet=true },{onSearch(it)})
    if(showBottomSheet){
        BottomSheetView(appList,onClick,onAppInfoClick,sheetState) {
            showBottomSheet = false
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(appListNew: MutableList<List<AppBlock>>, hotSeatAppList: MutableList<AppBlock>, modifier: Modifier, onClick:(String)-> Unit, onAppInfoClick:(String)->Unit,pagerState: PagerState,onSwipeUp: () -> Unit,onSearch:(String)-> Unit){
    Surface(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectVerticalDragGestures { change, dragAmount ->
                // If dragAmount is negative and significant (e.g., < -20)
                if (dragAmount < -20) {
                    onSwipeUp()
                }
            }
        },color= Color.White) {
        Column() {
            var text by remember { mutableStateOf("")}
            HorizontalPager(state = pagerState, Modifier.weight(1f)) { page ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(appListNew[page]) { appItem ->
                        AppItem(
                            appItem,
                            { onClick(appItem.packageName) },
                            { onAppInfoClick(appItem.packageName) })
                    }
                }
            }
            OutlinedTextField(
                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                shape = RoundedCornerShape(50),
                placeholder = {Text("Search something on web")},
                trailingIcon = {Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = "Search",
                    modifier = Modifier.clickable(){
                        onSearch(text)
                    }
                )}
            )

            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
                for(appItem in hotSeatAppList){
                    AppItem(appItem,
                        { onClick(appItem.packageName) },
                        { onAppInfoClick(appItem.packageName) })
                }
            }
        }
    }
}
