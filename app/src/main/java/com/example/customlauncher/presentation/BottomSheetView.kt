package com.example.customlauncher.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetView( appList: List<AppBlock>, onClick:(String)-> Unit, onAppInfoClick:(String)->Unit,sheetState: SheetState,onDismiss:()->Unit,onAddToFavorite:(AppBlock)-> Unit){
    //val sheetState = rememberModalBottomSheetState()
    //val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        BottomSheetContent(appList,onClick,onAppInfoClick,onAddToFavorite)
    }
}