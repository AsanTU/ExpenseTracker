package com.example.expensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentListOfExpensesBinding
import com.google.android.material.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth

class LoginScreenFragment : Fragment() {

    private var _binding: FragmentListOfExpensesBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationBtn()
//        onClickElements()
        navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreenFragment_to_listOfExpensesFragment)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun animationBtn() {
        binding.loginBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val scaleDown = android.view.animation.AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.scale_down
                    )
                    v.startAnimation(scaleDown)
                }

                MotionEvent.ACTION_UP -> {
                    val scaleUp = android.view.animation.AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.scale_up
                    )
                    v.startAnimation(scaleUp)
                }
            }
            false
        }
    }

    private fun onClickElements() {
        auth = FirebaseAuth.getInstance()
        binding.loginBtn.setOnClickListener { loginUser() }
        binding.registerBtn.setOnClickListener { registerUser() }
        binding.forgotPasswordTv.setOnClickListener { sendPasswordResetEmail() }
    }

    private fun loginUser() {
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailEt.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            binding.passwordEt.error = "Enter password"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "The login is completed", Toast.LENGTH_SHORT).show()
                    navigateToNextScreen()
                } else {
                    Toast.makeText(
                        context,
                        "Login error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun registerUser() {
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailEt.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            binding.passwordEt.error = "Enter password"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration is successful", Toast.LENGTH_SHORT).show()
                    navigateToNextScreen()
                } else {
                    Toast.makeText(
                        context,
                        "Registration error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun sendPasswordResetEmail() {
        val email = binding.emailEt.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailEt.error = "Enter email"
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "The password reset email has been sent",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Error sending the email: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}