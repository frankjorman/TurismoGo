package pe.turismogo.util

import android.content.Context
import android.content.Intent
import pe.turismogo.usecases.admin.home.HomeAdminActivity
import pe.turismogo.usecases.admin.panel.events.AdminEventsActivity
import pe.turismogo.usecases.user.home.HomeActivity
import pe.turismogo.usecases.login.LoginActivity
import pe.turismogo.usecases.policies.PrivacyPoliciesActivity
import pe.turismogo.usecases.policies.TermsAndConditionsActivity
import pe.turismogo.usecases.recover.RecoverUserActivity
import pe.turismogo.usecases.register.RegisterBusinessActivity
import pe.turismogo.usecases.register.RegisterSelectionActivity
import pe.turismogo.usecases.register.RegisterUserActivity
import pe.turismogo.usecases.user.dashboard.evaluate.UserEvaluateEventActivity
import pe.turismogo.usecases.user.dashboard.events.UserEventDetailsActivity


/***
 * Clase que permite reutilizar funciones de navegacion entre las actividades de la app
 */
class Navigation {

    companion object {
        /**
         * Navega al menú de Inicio de sesión
         */
        fun toLogin(context : Context) {
            val intent = Intent(context, LoginActivity::class.java)
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

        /**
         * Navega al menú de Terminos y condiciones
         */
        fun toTermsAndConditions(context: Context) {
            val intent = Intent(context, TermsAndConditionsActivity::class.java)
            context.startActivity(intent)
        }

        /**
         * Navega al menú de Politicas de Privacidad
         */
        fun toPrivacyPolicies(context: Context) {
            val intent = Intent(context, PrivacyPoliciesActivity::class.java)
            context.startActivity(intent)
        }

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

        /**
         * Navega al menu de detalles de evento para reservar
         */
        fun toEventDetails(context : Context) {
            val intent = Intent(context, UserEventDetailsActivity::class.java)
            context.startActivity(intent)
        }

        /**
         * Navega al menu de valoracion de actividad
         */
        fun toEventReview(context : Context) {
            val intent = Intent(context, UserEvaluateEventActivity::class.java)
            context.startActivity(intent)
        }
    }

}