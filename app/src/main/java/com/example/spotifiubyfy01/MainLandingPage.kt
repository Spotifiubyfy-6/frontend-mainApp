package com.example.spotifiubyfy01

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class MainLandingPage : AppCompatActivity() {


    private var gsc: GoogleSignInClient? = null
    private var gso: GoogleSignInOptions? = null
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_landing_page)

        val signUpClick = findViewById<Button>(R.id.sing_up_button)
        signUpClick.setOnClickListener {
            val intent = Intent(this, SignInLandingPage::class.java)
            startActivity(intent)
        }

        val logInClick = findViewById<Button>(R.id.log_in_button)
        logInClick.setOnClickListener {
            val intent = Intent(this, LogInPage::class.java)
            startActivity(intent)
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("428543728479-kbd5vr4aeethkhsa4tnlo1eqe058vnua.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.

        // Set the dimensions of the sign-in button.
        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
            try {
                val account = completedTask.getResult(ApiException::class.java)
                val idToken = account.idToken
                // ENVIAR TOKEN AL BACKEND PARA VALIDAR
                // Signed in successfully, show authenticated UI.
                validateToken(idToken)

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "###################################signInResult:failed code=" + e.statusCode)
                Toast.makeText(this, "ERROR",
                Toast.LENGTH_SHORT).show()

            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun validateToken(token : String?) {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/google_login/$token"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                // ################################
                // TODO: la respuesta es el token que tengo que guardar para poder hacer las requests
                //################################
                val sharedPref = getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString(getString(R.string.token_key), response.split('"')[3])
                    apply()
                }
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)

            },
            { errorResponse ->
                Toast.makeText(applicationContext, "Couldnt Login",
                    Toast.LENGTH_LONG).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }
        }
        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }
}