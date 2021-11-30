package com.example.pocketmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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

class RegisterFragment : Fragment() {
    // logging
    private val TAG = "Register"
    // view vars
    private lateinit var txtEmail:TextView
    private lateinit var txtPassword: TextView
    private lateinit var txtRepeat: TextView
    private lateinit var btnRegister: Button

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
        return inflater.inflate(R.layout.fragment_regsiter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtEmail = view.findViewById(R.id.txtRegisterEmail);
        txtPassword = view.findViewById(R.id.txtRegisterPassword)
        txtRepeat = view.findViewById(R.id.txtRegisterRepeat)
        btnRegister = view.findViewById(R.id.btnResiter)

        btnRegister.setOnClickListener(registerOnClickListener)

    }

    private val registerOnClickListener = View.OnClickListener {
        val email = txtEmail.text.toString()
        val pass = txtPassword.text.toString()
        val repeat = txtRepeat.text.toString()

        var correctFormat = false

        if(pass.length >= 6 && repeat.length >= 6){
            var correctPass = false
            var correctRepeat = false
            pass.forEach {
                if(it.isUpperCase()) correctPass = true
            }
            repeat.forEach {
                if(it.isUpperCase()) correctRepeat = true
            }
            correctFormat = correctPass && correctRepeat
        }

        if(email.isNullOrEmpty() || pass.isNullOrEmpty() || repeat.isNullOrEmpty()){
            Toast.makeText(context, "Please complete fields", Toast.LENGTH_SHORT).show()
        }else if(pass != repeat){
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
        }else if(!correctFormat){
            Toast.makeText(context, "Either password is not 6 characters long or does not have a capital letter", Toast.LENGTH_LONG).show()
        }
        else{
            // register and navigate
                MainActivity.progressBar.isVisible = true
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Cart.userId = auth.currentUser!!.uid
                            Log.d(TAG, "createUserWithEmail:success")
                            MainActivity.menuItem.isVisible = true
                            MainActivity.progressBar.isVisible = false
                            Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_mainFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            MainActivity.progressBar.isVisible = false
                        }
                    }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
            }
    }
}