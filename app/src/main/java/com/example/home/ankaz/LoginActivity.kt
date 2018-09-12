package com.example.home.ankaz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import java.sql.Connection
import android.widget.Toast
import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var success = false // boolean
    private var mAuthTask: UserLoginTask? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun checkLogin(view: View){

        // Reset errors.
        editLogin.error = null
        editPassword.error = null

        // Store values at the time of the login attempt.
        val loginStr = editLogin.text.toString()
        val passwordStr = editPassword.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            editPassword.error = getString(R.string.error_invalid_password)
            focusView = editPassword
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(loginStr)) {
            editLogin.error = getString(R.string.error_field_required)
            focusView = editLogin
            cancel = true
        } else if (!isLoginValid(loginStr)) {
            editLogin.error = getString(R.string.error_invalid_email)
            focusView = editLogin
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true)
            mAuthTask = UserLoginTask(loginStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }


    }

    fun mainPage(){
        if ( success  == true) {
            val randomIntent = Intent(this, MainActivity::class.java)
            startActivity(randomIntent)
        }
    }

    private fun isLoginValid(login: String): Boolean {
        //TODO: Replace this with your own logic
        //return email.contains("@")
        return login.length > 4
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 3
    }

    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    //internal inner class AsyncRequest : AsyncTask<String, Int, String>() {
    inner class UserLoginTask internal constructor(private val mLogin: String, private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        internal var z = ""
        internal var isSuccess: Boolean? = false

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                var con: Connection? = null
                var conn = Conn()
                con = conn.connectionclass()
                if (con == null) {
                    return false
                } else {

                    val query = "select * from users where user_name= '" + mLogin + "' and password = CONVERT(NVARCHAR(32),HashBytes('MD5', '" + mPassword + "'),2)"
                    val stmt = con.createStatement()
                    val rs = stmt.executeQuery(query)
                    if (rs.next()) {
                        success = true
                        return true
                    } else {
                        success = false
                        return false
                    }
                }
            } catch (e: InterruptedException) {
                return false
            }
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                Toast.makeText(this@LoginActivity, "Успешно", Toast.LENGTH_SHORT).show()
                mainPage()
                finish()
            } else {
                editPassword.error = getString(R.string.error_incorrect_password)
                editPassword.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)

        }
    }

}
