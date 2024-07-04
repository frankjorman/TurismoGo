package pe.turismogo.usecases.base

import pe.turismogo.util.Permissions

abstract class ActivityPermissions : ActivityBase() {

    fun requestStoragePermission() {
        if (Permissions.isSdk28orBelow())
            Permissions.Media.requestStoragePermissionForAPI29OrBelow(activity)
        else
            Permissions.Media.requestStoragePermissionsForAPI29OrAbove(activity)
    }

    fun isStorageGranted(): Boolean {
        return if (Permissions.isSdk28orBelow())
            Permissions.Media.checkIfStoragePermissionAreRequiredAPI29OrBelow(context)
        else
            Permissions.Media.checkIfStoragePermissionAreRequiredAPI29OrAbove(context)
    }
}