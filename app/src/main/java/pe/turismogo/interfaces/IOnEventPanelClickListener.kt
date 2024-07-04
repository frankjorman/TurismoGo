package pe.turismogo.interfaces

import pe.turismogo.model.domain.Event

interface IOnEventPanelClickListener {
    fun onEditClick(event : Event)
    fun onDeleteClick(event : Event)
}