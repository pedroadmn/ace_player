package br.com.pedroadmn.aceplayer.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import br.com.pedroadmn.aceplayer.R
import br.com.pedroadmn.aceplayer.databinding.FragmentRegistrationBinding
import br.com.pedroadmn.aceplayer.ui.login.LoginViewModel

class RegistrationFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    private lateinit var registrationBiding: FragmentRegistrationBinding

//    private val args: RegistrationFragmentArgs by navArgs()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        registrationBiding = FragmentRegistrationBinding.inflate(layoutInflater)
        return registrationBiding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

//        textChooseCredentialsName.text = getString(R.string.choose_credentials_text_name, args.name)

        val invalidFields = initValidationFields()
        listenToRegistrationStateEvent(invalidFields)
        registerViewListeners()
        registerDeviceBackStack()
    }

    private fun initValidationFields() = mapOf(
        RegistrationViewModel.INPUT_NAME.first to registrationBiding.etRegisterName,
        RegistrationViewModel.INPUT_EMAIL.first to registrationBiding.etRegisterEmail,
        RegistrationViewModel.INPUT_PASSWORD.first to registrationBiding.etRegisterPassword
    )

    private fun listenToRegistrationStateEvent(validationFields: Map<String, EditText>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.RegistrationCompleted -> {
                    val token = registrationViewModel.authToken
                    val username = registrationBiding.etRegisterName.text.toString()

                    loginViewModel.authenticateToken(token, username)
                    navController.popBackStack(R.id.homeFragment, false)
                }

                is RegistrationViewModel.RegistrationState.InvalidCredentials -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

    private fun registerViewListeners() {
        registrationBiding.btRegister.setOnClickListener {
            val username = registrationBiding.etRegisterName.text.toString()
            val email = registrationBiding.etRegisterEmail.text.toString()
            val password = registrationBiding.etRegisterPassword.text.toString()

            registrationViewModel.createCredentials(username, email, password)
        }

        registrationBiding.etRegisterName.addTextChangedListener {
//            inputLayoutChooseCredentialsUsername.dismissError()
        }

        registrationBiding.etRegisterPassword.addTextChangedListener {
//            inputLayoutChooseCredentialsPassword.dismissError()
        }
    }

    private fun registerDeviceBackStack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return super.onOptionsItemSelected(item)
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        navController.popBackStack(R.id.loginFragment, false)
    }
}