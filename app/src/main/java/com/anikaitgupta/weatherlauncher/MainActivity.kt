package com.anikaitgupta.weatherlauncher

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.os.UserManager
import android.provider.MediaStore
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anikaitgupta.weatherlauncher.presentation.AppBlock
import com.anikaitgupta.weatherlauncher.presentation.AppList
import com.anikaitgupta.weatherlauncher.presentation.LauncherViewModel
import com.anikaitgupta.weatherlauncher.presentation.WeatherScreen
import com.anikaitgupta.weatherlauncher.ui.theme.CustomLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

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
        // Usage in your loop:
        val dialerPkg = getPackageForIntent(applicationContext, Intent.ACTION_DIAL)
        val cameraPkg = getPackageForIntent(applicationContext, MediaStore.ACTION_IMAGE_CAPTURE)
        val chromePkg = "com.android.chrome" // Chrome is usually safe to hardcode as an extra

        for(app in activities){
            if(app.applicationInfo.packageName != this.packageName) {
                val appItem = AppBlock(
                    appName = app.label.toString(),
                    //icon = app.getIcon(0),
                    packageName = app.applicationInfo.packageName
                )
                if(appItem.appName == "Phone" || appItem.appName =="Contacts" || appItem.appName == "Camera" || appItem.appName=="Chrome" || appItem.packageName==dialerPkg || appItem.packageName==cameraPkg || appItem.packageName==chromePkg){
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
                val launcherViewModel: LauncherViewModel = hiltViewModel()
                val screenToShow = launcherViewModel.screenToShowFlow.collectAsStateWithLifecycle().value
                Scaffold(containerColor = if(screenToShow==0)Color.Transparent else MaterialTheme.colorScheme.background) { innerPadding ->
                    val listOfFavoriteApps =launcherViewModel.listOfFavoriteApps.collectAsStateWithLifecycle().value
                    val hasSeenDisclosure = launcherViewModel.hasSeenDisclosure.collectAsStateWithLifecycle().value
                    var showDialog by remember { mutableStateOf(false) }
                    var showLauncherDialog by remember { mutableStateOf(false) }
                    var onDismiss by rememberSaveable{mutableStateOf(0)}

                    LaunchedEffect(hasSeenDisclosure) {
                        if (!hasSeenDisclosure) showDialog = true
                        else showDialog = false
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { /* Don't allow dismiss by tapping outside */ },
                            title = { Text("Privacy & App Access") },
                            text = { Text("To work as a launcher, this app needs to see your installed apps to display them on your home screen. We also use your manually entered city to fetch weather data. No data is sent to our servers; it stays on your device.") },
                            confirmButton = {
                                Button(onClick = {
                                     launcherViewModel.onDisclosureAccepted()
                                    showDialog = false
                                }) { Text("I Agree") }
                            }
                        )
                    }
                    if (onDismiss==0 && !isMyLauncherDefault(this)) {
                        showLauncherDialog = true
                    }

                    if (showLauncherDialog) {
                        AlertDialog(
                            onDismissRequest = { showLauncherDialog = false },
                            title = { Text("Set as Default Launcher") },
                            text = { Text("To experience the full features of this app, please set it as your default home screen.") },
                            confirmButton = {
                                Button(onClick = {
                                    showLauncherDialog = false
                                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                    startActivity(intent)
                                }) { Text("Set Default") }
                            },
                            dismissButton = {
                                Button(onClick = { showLauncherDialog = false }) {
                                    Text("Maybe Later")
                                    onDismiss=1
                                }
                            }
                        )
                    }
                    if(screenToShow==0) {
                        AppList(
                            appList,
                            listOfFavoriteApps,
                            hotSeatAppList,
                            Modifier.padding(innerPadding),
                            {
                                this.startActivity(
                                    packageManager.getLaunchIntentForPackage(it)
                                )
                            },
                            {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.data = "package:$it".toUri()
                                // Use addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) if calling from a non-Activity context
                                this.startActivity(intent)
                            },
                            {
                                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                                intent.putExtra(
                                    SearchManager.QUERY,
                                    it
                                ) // query contains search string
                                startActivity(intent)
                            },
                            { launcherViewModel.addFavoriteApp(it) },
                            { launcherViewModel.removeFavoriteApp(it) },{launcherViewModel.updateScreenToShow(1)})
                    }
                    else if(screenToShow==1){
                        WeatherScreen({launcherViewModel.updateScreenToShow(0)},Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
fun isMyLauncherDefault(context: Context): Boolean {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
    val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return resolveInfo?.activityInfo?.packageName == context.packageName
}
fun getPackageForIntent(context: Context, action: String): String? {
    val intent = Intent(action)
    val resolveInfo = context.packageManager.resolveActivity(
        intent,
        PackageManager.MATCH_DEFAULT_ONLY
    )
    return resolveInfo?.activityInfo?.packageName
}



