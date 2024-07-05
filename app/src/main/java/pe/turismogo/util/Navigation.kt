package pe.turismogo.util

import android.content.Context
import android.content.Intent
import pe.turismogo.model.domain.Event
import pe.turismogo.usecases.business.home.HomeAdminActivity
import pe.turismogo.usecases.business.panel.create_events.AdminEventsActivity
import pe.turismogo.usecases.business.panel.edit_events.AdminEditEventsActivity
import pe.turismogo.usecases.business.profile.BusinessEditActivity
import pe.turismogo.usecases.user.home.HomeActivity
import pe.turismogo.usecases.login.LoginActivity
import pe.turismogo.usecases.recover.RecoverUserActivity
import pe.turismogo.usecases.register.RegisterBusinessActivity
import pe.turismogo.usecases.register.RegisterSelectionActivity
import pe.turismogo.usecases.register.RegisterUserActivity
import pe.turismogo.usecases.user.dashboard.reservations.UserReservationsDetailsActivity
import pe.turismogo.usecases.user.dashboard.reviews.UserEvaluateEventActivity
import pe.turismogo.usecases.user.events.details.UserEventDetailsActivity
import pe.turismogo.usecases.user.profile.EditUserActivity


/***
 * Clase que permite reutilizar funciones de navegacion entre las actividades de la app
 */
object Navigation {
    /**
     * Navega al menú de Inicio de sesión
     */
    fun toLogin(context : Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * Navega al menú de Selección de Registro de usuario.
     */
    fun toRegisterSelection(context : Context) {
        val intent = Intent(context, RegisterSelectionActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menú de Registro de usuario.
     */
    fun toRegisterUser(context : Context) {
        val intent = Intent(context, RegisterUserActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menú de Registro de Empresa
     */
    fun toRegisterBusiness(context : Context) {
        val intent = Intent(context, RegisterBusinessActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menú de Recuperación de contraseña
     */
    fun toRecoverPassword(context: Context) {
        val intent = Intent(context, RecoverUserActivity::class.java)
        context.startActivity(intent)
    }

//    /**
//     * Navega al menú de Terminos y condiciones
//     */
//    fun toTermsAndConditions(context: Context) {
//        val intent = Intent(context, TermsAndConditionsActivity::class.java)
//        context.startActivity(intent)
//    }
//
//    /**
//     * Navega al menú de Politicas de Privacidad
//     */
//    fun toPrivacyPolicies(context: Context) {
//        val intent = Intent(context, PrivacyPoliciesActivity::class.java)
//        context.startActivity(intent)
//    }

    /**
     * Navega al menú principal del usuario
     */
    fun toUserHomeMenu(context : Context) {
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menú principal del administrador
     */
    fun toAdminHomeMenu(context : Context) {
        val intent = Intent(context, HomeAdminActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menú principal del administrador para agregar o editar Eventos
     */
    fun toEventAdd(context : Context) {
        val intent = Intent(context, AdminEventsActivity::class.java)
        context.startActivity(intent)
    }

    fun toEventEdit(context : Context) {
        val intent = Intent(context, AdminEditEventsActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menu de detalles de evento para reservar
     */
    fun toEventDetails(context : Context) {
        val intent = Intent(context, UserEventDetailsActivity::class.java)
        context.startActivity(intent)
    }

    fun toReservationDetails(context : Context) {
        val intent = Intent(context, UserReservationsDetailsActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Navega al menu de valoracion de actividad
     */
    fun toEventReview(context : Context) {
        val intent = Intent(context, UserEvaluateEventActivity::class.java)
        context.startActivity(intent)
    }

    fun toEditUser(context: Context) {
        val intent = Intent(context, EditUserActivity::class.java)
        context.startActivity(intent)

    }

    fun toEditBusiness(context: Context) {
        val intent = Intent(context, BusinessEditActivity::class.java)
        context.startActivity(intent)
    }

}