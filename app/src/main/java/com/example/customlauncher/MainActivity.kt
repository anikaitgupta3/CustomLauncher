package com.example.customlauncher

import android.app.SearchManager
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.Process
import android.os.UserManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.example.customlauncher.ui.theme.CustomLauncherTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*val resolvedApplist:List<ResolveInfo> = packageManager
            .queryIntentActivities(Intent(Intent.ACTION_MAIN,null)
                .addCategory(Intent.CATEGORY_LAUNCHER),0)
        val appList = mutableListOf<AppBlock>()
        for (ri in resolvedApplist) {
            if(ri.activityInfo.packageName!=this.packageName) {
                val app = AppBlock(
                    ri.loadLabel(packageManager).toString(),
                    ri.activityInfo.loadIcon(packageManager),
                    ri.activityInfo.packageName
                )
                appList.add(app)
            }
        }*/
        val launcherApps = getSystemService(LAUNCHER_APPS_SERVICE) as LauncherApps
        val userManager = getSystemService(USER_SERVICE) as UserManager
        val userHandle = Process.myUserHandle()
        val activities = launcherApps.getActivityList(null, userHandle)
        val appList = mutableListOf<AppBlock>()
        val hotSeatAppList = mutableListOf<AppBlock>()



        /*val packman = packageManager
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packCamera = intentCamera.resolveActivity(packman).packageName
        val intentDial = Intent(Intent.ACTION_DIAL)
        val packPhone = intentDial.resolveActivity(packman).packageName*/



        for(app in activities){
            if(app.applicationInfo.packageName != this.packageName) {
                val appItem = AppBlock(
                    appName = app.label.toString(),
                    icon = app.getIcon(0),
                    packageName = app.applicationInfo.packageName
                )
                if(appItem.appName == "Phone" || appItem.appName =="Contacts" || appItem.appName == "Camera" || appItem.appName=="Chrome"){
                    hotSeatAppList.add(appItem)
                }
                else {
                    appList.add(appItem)
                }
            }
        }
        appList.sortBy{it.appName}
        val halfSize = appList.size / 2

        // First half of the list
        val firstHalf = appList.take(halfSize)
        // Second half of the list
        val secondHalf = appList.drop(halfSize)
        val appListNew = mutableListOf<List<AppBlock>>()
        appListNew.add(firstHalf)
        appListNew.add(secondHalf)


        setContent {
            CustomLauncherTheme {
                Scaffold() { innerPadding ->
                    AppList(appList,appListNew,hotSeatAppList,Modifier.padding(innerPadding),{
                        this.startActivity(
                            packageManager.getLaunchIntentForPackage(it)
                        )
                    },{
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = "package:$it".toUri()
                        // Use addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) if calling from a non-Activity context
                        this.startActivity(intent)
                    },{
                        val intent = Intent(Intent.ACTION_WEB_SEARCH)
                        intent.putExtra(SearchManager.QUERY, it) // query contains search string
                        startActivity(intent)
                    })
                }
            }
        }
    }
}

