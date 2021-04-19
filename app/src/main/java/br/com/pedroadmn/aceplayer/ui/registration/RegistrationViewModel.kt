package br.com.pedroadmn.aceplayer.ui.registration

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import br.com.pedroadmn.aceplayer.R

class RegistrationViewModel(/*private val userRepository: UserRepository*/) : ViewModel() {

    sealed class RegistrationState {
        object CollectProfileData :  RegistrationState()
        object RegistrationCompleted : RegistrationState()
        class InvalidCredentials(val fields: List<Pair<String, Int>>) : RegistrationState()
    }

    private val _registrationStateEvent = MutableLiveData<RegistrationState>(RegistrationState.CollectProfileData)

    val registrationStateEvent: LiveData<RegistrationState>
        get() = _registrationStateEvent

    private val registrationViewParams = RegistrationViewParams()

    var authToken = ""
        private set

    fun createCredentials(name: String, email: String, password: String) {
        if (isValidCredentials(name, email, password)) {
            viewModelScope.launch {
                registrationViewParams.name = name
                registrationViewParams.email = email
                registrationViewParams.password = password

//                userRepository.createUser(registrationViewParams)

                this@RegistrationViewModel.authToken = "token"
                _registrationStateEvent.value = RegistrationState.RegistrationCompleted
            }
        }
    }

    private fun isValidCredentials(name: String, email: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (name.isEmpty()) {
            invalidFields.add(INPUT_NAME)
        }

        if (email.isEmpty()) {
            invalidFields.add(INPUT_EMAIL)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }


        if (invalidFields.isNotEmpty()) {
            _registrationStateEvent.value = RegistrationState.InvalidCredentials(invalidFields)
            return false
        }

        return true
    }

    fun userCancelledRegistration() : Boolean {
        authToken = ""
        _registrationStateEvent.value = RegistrationState.CollectProfileData
        return true
    }

    companion object {
        val INPUT_NAME = "INPUT_NAME" to R.string.profile_data_input_layout_error_invalid_name
        val INPUT_EMAIL = "INPUT_EMAIL" to R.string.profile_data_input_layout_error_invalid_email
        val INPUT_PASSWORD = "INPUT_PASSWORD" to R.string.profile_data_input_layout_error_invalid_password
    }

//    class RegistrationViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return RegistrationViewModel(userRepository) as T
//        }
//    }
}