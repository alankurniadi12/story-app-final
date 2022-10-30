package com.alankurniadi.storyapp.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.dataStore
import com.alankurniadi.storyapp.databinding.FragmentRegisterBinding
import com.alankurniadi.storyapp.home.ListStoryViewModel
import com.alankurniadi.storyapp.utils.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val registerVm =
            ViewModelProvider(this, ViewModelFactory(pref))[RegisterViewModel::class.java]
        val listStoryVm =
            ViewModelProvider(this, ViewModelFactory(pref))[ListStoryViewModel::class.java]

        listStoryVm.saveToken("")

        with(binding) {
            edRegisterName.doOnTextChanged { _, _, _, count ->
                if (count == 0) {
                    setDisableButton(btnRegister)
                    tilRegName.isErrorEnabled = true
                    tilRegName.error = getString(R.string.label_error_message_name)
                } else {
                    setEnableButton(btnRegister)
                    tilRegName.isErrorEnabled = false
                }
            }

            edRegisterEmail.doOnTextChanged { text, _, _, _ ->
                if (!isEmailValid(text.toString())) {
                    setDisableButton(btnRegister)
                    tilRegEmail.isErrorEnabled = true
                    tilRegEmail.error = getString(R.string.label_error_message_email_failed)
                } else {
                    setEnableButton(btnRegister)
                    tilRegEmail.isErrorEnabled = false
                }
            }

            edRegisterPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.length < 6) {
                    setDisableButton(btnRegister)
                    tilRegPassword.isErrorEnabled = true
                    tilRegPassword.error = getString(R.string.label_error_message_password)
                } else {
                    setEnableButton(btnRegister)
                    tilRegPassword.isErrorEnabled = false
                }
            }

            btnRegister.setOnClickListener { view ->
                registerProgress.visibility = View.VISIBLE
                val name = edRegisterName.text.toString().trim()
                val email = edRegisterEmail.text.toString().trim()
                val password = edRegisterPassword.text.toString().trim()

                if (name.isEmpty()) {
                    tilRegName.isErrorEnabled = true
                    tilRegName.error = getString(R.string.label_error_message_name)
                    setDisableButton(btnRegister)
                    registerProgress.visibility = View.GONE
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    tilRegEmail.isErrorEnabled = true
                    tilRegEmail.error = getString(R.string.label_error_message_email_empty)
                    setDisableButton(btnRegister)
                    registerProgress.visibility = View.GONE
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    tilRegPassword.isErrorEnabled = true
                    tilRegPassword.error = getString(R.string.label_error_message_password)
                    setDisableButton(btnRegister)
                    registerProgress.visibility = View.GONE
                    return@setOnClickListener
                }
                registerVm.postRegister(name, email, password)
            }

            registerVm.register.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                if (it.error == false) {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                registerProgress.visibility = View.GONE
            }

            registerVm.message.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "$it,\nCoba lagi dengan nama lain", Toast.LENGTH_LONG).show()
                registerProgress.visibility = View.GONE
            }

            tvLogin.setOnClickListener {
                it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
