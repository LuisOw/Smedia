package com.example.smedia

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials

class MainViewModel: ViewModel() {

    var appJustLaunched by mutableStateOf(true)
    var userIsAuthenticated by mutableStateOf(false)
    var user by mutableStateOf(User())

    private val TAG = "MainViewModel"
    private lateinit var account: Auth0

    fun setAccountReference(activityContext: Context) {
        account = Auth0(
            activityContext.getString(R.string.com_auth0_client_id),
            activityContext.getString(R.string.com_auth0_domain)
        )
    }

    fun login(context: Context) {
        WebAuthProvider
            .login(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    Log.e(TAG, "Error occurred in login(): $error")
                }

                override fun onSuccess(result: Credentials) {
                    val idToken = result.idToken
                    Log.d(TAG, "user token = $idToken")
                    user = User(idToken)
                    userIsAuthenticated = true
                    appJustLaunched = false
                }
            })
    }

    fun logout(context: Context) {
        WebAuthProvider
            .logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    // For some reason, logout failed.
                    Log.e(TAG, "Error occurred in logout(): $error")
                }

                override fun onSuccess(result: Void?) {
                    // The user successfully logged out.
                    userIsAuthenticated = false
                    user = User()
                }

            })
    }
}