package br.com.pedroadmn.aceplayer.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.pedroadmn.aceplayer.R

class LoginViewModel : ViewModel() {

    sealed class AuthenticationState {
        object Unauthenticated : AuthenticationState()
        object Authenticated : AuthenticationState()
        class InvalidAuthentication(val fields: List<Pair<String, Int>>) : AuthenticationState()
    }

    var email: String = ""
    var token: String = ""

    private val _authenticationStateEvent = MutableLiveData<AuthenticationState>()

    val authenticationStateEvent: LiveData<AuthenticationState>
        get() = _authenticationStateEvent

    init {
        refuseAuthentication()
    }

    fun refuseAuthentication() {
        _authenticationStateEvent.value = AuthenticationState.Unauthenticated
    }

    fun authenticateToken(token: String, username: String) {
        this.token = token
        this.email = username
        _authenticationStateEvent.value = AuthenticationState.Authenticated
    }

    fun authenticate(email: String, password: String) {
        if (isValidForm(email, password)) {
            this.email = email
            _authenticationStateEvent.value = AuthenticationState.Authenticated
        }
    }

    private fun isValidForm(username: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (username.isEmpty()) {
            invalidFields.add(INPUT_EMAIL)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {
            _authenticationStateEvent.value =
                AuthenticationState.InvalidAuthentication(invalidFields)
            return false
        }

        return true
    }

    companion object {
        val INPUT_EMAIL = "INPUT_EMAIL" to R.string.login_input_layout_error_invalid_email
        val INPUT_PASSWORD = "INPUT_PASSWORD" to R.string.login_input_layout_error_invalid_password
    }
}