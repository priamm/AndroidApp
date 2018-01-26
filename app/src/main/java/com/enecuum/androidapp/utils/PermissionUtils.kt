package com.enecuum.androidapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.enecuum.androidapp.application.EnecuumApplication

/**
 * Created by oleg on 23.01.18.
 */
object PermissionUtils {
    const val PermissionsRequestCode = 1024
    val storagePermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun checkPermissions(permissions: Array<String>) : Boolean {
        var result: Int
        for (permission in permissions) {
            result = ContextCompat.checkSelfPermission(EnecuumApplication.applicationContext(), permission)
            if (result != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    fun requestPermissions(activity: Activity, permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, PermissionsRequestCode)
    }

    fun requestPermissions(fragment: Fragment, permissions: Array<String>) {
        fragment.requestPermissions(permissions, PermissionsRequestCode)
    }

    fun handleGrantResults(results: IntArray) : Boolean {
        for(result in results) {
            if(result != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    fun checkPermissionsAndRunFunction(function: () -> Unit, requestCode : Int, grantResults : IntArray) {
        if(requestCode != PermissionUtils.PermissionsRequestCode)
            return
        if(PermissionUtils.handleGrantResults(grantResults)) {
            function()
        }
    }
}