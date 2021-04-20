package br.com.pedroadmn.aceplayer.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.pedroadmn.aceplayer.R
import br.com.pedroadmn.aceplayer.databinding.FragmentLoginBinding
import br.com.pedroadmn.aceplayer.extensions.navigateWithAnimations
import kotlinx.android.synthetic.main.activity_main.*

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var loginBiding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginBiding = FragmentLoginBinding.inflate(layoutInflater)
        return loginBiding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.bottomNavigation?.visibility = View.GONE

        viewModel.authenticationStateEvent.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
                    is LoginViewModel.AuthenticationState.InvalidAuthentication -> {
                        val validationFields: Map<String, EditText> = initValidationFields()

                        authenticationState.fields.forEach { errorField ->
                            validationFields[errorField.first]?.error = getString(errorField.second)
                        }
                    }

                    is LoginViewModel.AuthenticationState.Authenticated -> {
                        findNavController().popBackStack()
                    }
                }
            })

        loginBiding.tvRegister.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_loginFragment_to_registrationFragment)
        }

        loginBiding.btEnter.setOnClickListener {
            val email = loginBiding.etLoginEmail.text.toString()
            val password = loginBiding.etLoginPassword.text.toString()

            viewModel.authenticate(email, password)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }

    private fun initValidationFields() = mapOf(
        LoginViewModel.INPUT_EMAIL.first to loginBiding.etLoginEmail,
        LoginViewModel.INPUT_PASSWORD.first to loginBiding.etLoginPassword
    )
}