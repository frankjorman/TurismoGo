package pe.turismogo.interfaces

import pe.turismogo.model.domain.Event

interface IOnEventClickListener {
    fun onClickDetails(event : Event)
}