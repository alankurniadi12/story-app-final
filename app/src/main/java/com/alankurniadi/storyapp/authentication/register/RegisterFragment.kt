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
            ViewModelProvider(this, ViewModelFactory(requireContext(), pref))[RegisterViewModel::class.java]
        val listStoryVm =
            ViewModelProvider(this, ViewModelFactory(requireContext(), pref))[ListStoryViewModel::class.java]

        listStoryVm.saveToken("")

        with(binding) {
            edRegisterName.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    setEnableButton(btnRegister)
                } else {
                    setDisableButton(btnRegister)
                }
            }

            edRegisterEmail.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    setEnableButton(btnRegister)
                } else {
                    setDisableButton(btnRegister)
                }
            }

            edRegisterPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    setEnableButton(btnRegister)
                } else {
                    setDisableButton(btnRegister)
                }
            }

            btnRegister.setOnClickListener { view ->
                registerProgress.visibility = View.VISIBLE
                val name = edRegisterName.text.toString().trim()
                val email = edRegisterEmail.text.toString().trim()
                val password = edRegisterPassword.text.toString().trim()

                if (name.isEmpty()) {
                    edRegisterName.error = getString(R.string.label_error_message_name)
                    setDisableButton(btnRegister)
                    registerProgress.visibility = View.GONE
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    edRegisterEmail.error = getString(R.string.label_error_message_email_empty)
                    setDisableButton(btnRegister)
                    registerProgress.visibility = View.GONE
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    edRegisterPassword.error = getString(R.string.label_error_message_password_empty)
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
