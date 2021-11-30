package com.example.pocketmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    // logging
    private val TAG = "Login"

    // view variables
    private lateinit var txtEmail: TextView
    private lateinit var txtPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var lblRegister: TextView

    // firebase vars
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // defining the variables
        txtEmail = view.findViewById(R.id.txtRegisterEmail)
        txtPassword = view.findViewById(R.id.txtRegisterPassword)
        btnLogin = view.findViewById(R.id.btnResiter)
        btnLogin.setOnClickListener(loginOnClickListener)

        lblRegister = view.findViewById(R.id.lblRegister)
        lblRegister.setOnClickListener(navigateToRegistration)
    }

    private val loginOnClickListener = View.OnClickListener {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        var correctPass = false
        if(password.length >= 6){
            password.forEach {
                if(it.isUpperCase()) correctPass = true
            }
        }

        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            Toast.makeText(context, "Please complete information", Toast.LENGTH_SHORT).show()
        }else if(!correctPass){
            Toast.makeText(context, "Password too short or does not have capital letter", Toast.LENGTH_LONG).show()
        }
        else{
            MainActivity.progressBar.isVisible = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Cart.userId = auth.currentUser!!.uid
                        MainActivity.menuItem.isVisible = true
                        MainActivity.progressBar.isVisible = false
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        MainActivity.progressBar.isVisible = false
                    }
                }
        }
    }

    private val navigateToRegistration = View.OnClickListener {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}