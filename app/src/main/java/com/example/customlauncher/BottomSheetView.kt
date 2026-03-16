package com.example.customlauncher

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetView( appList: MutableList<AppBlock>, onClick:(String)-> Unit, onAppInfoClick:(String)->Unit,sheetState: SheetState,onDismiss:()->Unit){
    //val sheetState = rememberModalBottomSheetState()
    //val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        BottomSheetContent(appList,onClick,onAppInfoClick)
    }
}