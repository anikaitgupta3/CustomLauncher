package com.example.customlauncher

import android.app.SearchManager
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.Process
import android.os.UserManager
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.customlauncher.presentation.AppBlock
import com.example.customlauncher.presentation.AppList
import com.example.customlauncher.presentation.LauncherViewModel
import com.example.customlauncher.ui.theme.CustomLauncherTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Set the window to be translucent
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER,
            WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
        )


        val launcherApps = getSystemService(LAUNCHER_APPS_SERVICE) as LauncherApps
        val userManager = getSystemService(USER_SERVICE) as UserManager
        val userHandle = Process.myUserHandle()
        val activities = launcherApps.getActivityList(null, userHandle)
        val appList = mutableListOf<AppBlock>()
        val hotSeatAppList = mutableListOf<AppBlock>()
        for(app in activities){
            if(app.applicationInfo.packageName != this.packageName) {
                val appItem = AppBlock(
                    appName = app.label.toString(),
                    //icon = app.getIcon(0),
                    packageName = app.applicationInfo.packageName
                )
                if(appItem.appName == "Phone" || appItem.appName =="Contacts" || appItem.appName == "Camera" || appItem.appName=="Chrome"){
                    hotSeatAppList.add(appItem)
                }
                appList.add(appItem)
            }
        }
        appList.sortBy{it.appName}
        /*val halfSize = appList.size / 2

        // First half of the list
        val firstHalf = appList.take(halfSize)
        // Second half of the list
        val secondHalf = appList.drop(halfSize)
        val appListNew = mutableListOf<List<AppBlock>>()
        appListNew.add(firstHalf)
        appListNew.add(secondHalf)*/


        setContent {
            CustomLauncherTheme {
                Scaffold() { innerPadding ->
                    val launcherViewModel: LauncherViewModel = hiltViewModel()
                    val listOfFavoriteApps =launcherViewModel.listOfFavoriteApps.collectAsStateWithLifecycle().value
                    AppList(appList, listOfFavoriteApps, hotSeatAppList, Modifier.padding(innerPadding), {
                        this.startActivity(
                            packageManager.getLaunchIntentForPackage(it)
                        )
                    }, {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = "package:$it".toUri()
                        // Use addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) if calling from a non-Activity context
                        this.startActivity(intent)
                    }, {
                        val intent = Intent(Intent.ACTION_WEB_SEARCH)
                        intent.putExtra(SearchManager.QUERY, it) // query contains search string
                        startActivity(intent)
                    },{launcherViewModel.addFavoriteApp(it)},{launcherViewModel.removeFavoriteApp(it)})
                }
            }
        }
    }
}

