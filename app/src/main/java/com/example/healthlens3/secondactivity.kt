package com.example.healthlens3

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

@Composable
fun secondactivity(
    onLoginSuccess: (String) -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()

    val focusManager = LocalFocusManager.current

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }



//    LaunchedEffect(auth.currentUser) {
//        auth.currentUser?.let {
//            // If the user is already logged in, navigate to the home page
//            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//            onLoginSuccess(userId) // Pass userId to the lambda function
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(emailState.value, passwordState.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Extract the user ID from Firebase Auth
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            // Check if userId is not null
                            if (userId != null) {
                                // Correctly pass the userId to the userProfileAndHistory route
                                onLoginSuccess(userId)
                            } else {
                                loginErrorMessage = "User ID not found"
                            }
                        } else {
                            task.exception?.let {
                                if (it is FirebaseAuthInvalidCredentialsException) {
                                    // Show the toast only if it's an invalid credentials error
                                    Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                                }
                                loginErrorMessage = it.localizedMessage
                            }
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        loginErrorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSignUpClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }
    }
}

@Preview
@Composable
fun LoginPagePreview() {
    secondactivity()
}
