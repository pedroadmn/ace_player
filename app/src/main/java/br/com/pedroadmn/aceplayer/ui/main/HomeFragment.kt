package br.com.pedroadmn.aceplayer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.pedroadmn.aceplayer.R
import br.com.pedroadmn.aceplayer.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class HomeFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        loginViewModel.authenticationStateEvent.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
                    is LoginViewModel.AuthenticationState.Authenticated -> {
                        activity?.bottomNavigation?.visibility = View.VISIBLE
                        activity?.toolbar?.visibility = View.VISIBLE
//                    tvWelcome.text = getString(R.string.profile_text_welcome, loginViewModel.email)
                    }

                    is LoginViewModel.AuthenticationState.Unauthenticated -> {
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            })
    }
}