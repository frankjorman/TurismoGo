package pe.turismogo.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import java.util.Arrays

object Permissions {

    const val DENIED: Int = -1
    const val GRANTED: Int = 0

    const val REQUEST_PERMISSIONS_NORMAL_LEGACY: Int = 1000
    const val REQUEST_PERMISSION_NORMAL: Int = 1001
    const val REQUEST_PERMISSIONS_LOCATION: Int = 1002
    const val REQUEST_PERMISSION_LOCATION_BACKGROUND: Int = 1003
    const val REQUEST_PERMISSION_POST_NOTIFICATION: Int = 1004
    const val REQUEST_PERMISSION_STORAGE_LEGACY: Int = 1005
    const val REQUEST_PERMISSION_STORAGE: Int = 1006
    const val REQUEST_PERMISSION_BATTERY: Int = 1007
    const val ACTIVATE_LOCATION_REQUEST_CODE: Int = 1008

    val PERMISSION_READ_WRITE_EXTERNAL_STORAGE: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @SuppressLint("InlinedApi")
    val PERMISSION_MEDIA_FILES: Array<String> = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    fun isSdk28orBelow(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    }

    fun isSdk33orAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun checkIsPermissionMissing(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun checkIsPermissionMissing(context: Context, vararg permissions: String): Boolean {
        return Arrays.stream<String>(permissions)
            .anyMatch { permission: String -> checkIsPermissionMissing (context, permission)
        }
    }

    object Media {
        fun checkIfStoragePermissionAreRequiredAPI29OrBelow(context: Context): Boolean {
            return checkIsPermissionMissing(context,
                PERMISSION_READ_WRITE_EXTERNAL_STORAGE.toString()
            )
        }

        fun requestStoragePermissionForAPI29OrBelow(activity: Activity) {
            ActivityCompat.requestPermissions(activity,
                PERMISSION_READ_WRITE_EXTERNAL_STORAGE,
                REQUEST_PERMISSION_STORAGE_LEGACY
            )
        }

        fun checkIfStoragePermissionAreRequiredAPI29OrAbove(context: Context): Boolean {
            return checkIsPermissionMissing(context, PERMISSION_MEDIA_FILES.toString())
        }

        fun requestStoragePermissionsForAPI29OrAbove(activity: Activity) {
            ActivityCompat.requestPermissions(activity,
                PERMISSION_MEDIA_FILES,
                REQUEST_PERMISSION_STORAGE
            )
        }
    }
}