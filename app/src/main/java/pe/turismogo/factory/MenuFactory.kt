package pe.turismogo.factory

import android.view.View
import androidx.appcompat.widget.PopupMenu
import pe.turismogo.R

class MenuFactory {

    companion object {
        const val TYPE_PANEL_FILTER = "typePanelFilter"
    }

    fun getMenu(parent : View, type : String) : PopupMenu {
        val popupMenu = PopupMenu(parent.context, parent)
        when(type) {
            TYPE_PANEL_FILTER -> {
                popupMenu
                    .menuInflater
                    .inflate(R.menu.menu_business_panel_filter, popupMenu.menu)
                return popupMenu
            }
            else -> {
                return popupMenu
            }
        }
    }

}