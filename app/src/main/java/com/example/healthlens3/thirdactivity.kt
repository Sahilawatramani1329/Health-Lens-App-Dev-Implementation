package com.example.healthlens3

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun thirdactivity(onSignUpClick: (String) -> Unit={}) {
    val focusManager = LocalFocusManager.current

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    val nameState = remember { mutableStateOf("") }
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val ageState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val termsAndConditionsState = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",  fontSize = 69.sp, color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = usernameState.value,
            onValueChange = { usernameState.value = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = ageState.value,
            onValueChange = { ageState.value = it },
            label = { Text("Age") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        TextField(
            value = genderState.value,
            onValueChange = { genderState.value = it },
            label = { Text("Gender") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAndConditionsState.value,
                onCheckedChange = { termsAndConditionsState.value = it }
            )

            Text(
                text = "I agree to the terms and conditions",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Button(
            onClick = {
                // Perform user sign-up with FirebaseAuth
                auth.createUserWithEmailAndPassword(emailState.value, passwordState.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // On successful sign-up, get the user's UID
                            val userId = task.result?.user?.uid
                            userId?.let { uid ->
                                // Save additional user information in FirebaseDatabase under the user's UID
                                val userMap = mapOf(
                                    "name" to nameState.value,
                                    "username" to usernameState.value,
                                    "email" to emailState.value, // Email is directly taken from the input
                                    "age" to ageState.value,
                                    "gender" to genderState.value,
                                    "agreedToTerms" to termsAndConditionsState.value
                                )
                                usersRef.child(uid).setValue(userMap)
                                    .addOnSuccessListener {
                                        // If user info is successfully saved, navigate to the user profile page
                                        onSignUpClick(uid) // Pass the userId for navigation
                                    }
                                    .addOnFailureListener { e ->
                                        // Log or show the error when saving user info fails
                                        Log.e("SignUp", "Failed to save user info: ${e.message}")
                                    }
                            }
                        } else {
                            // If sign-up fails, log or show error
                            Log.e("SignUp", "Sign-up failed: ${task.exception?.message}")
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Sign Up")
        }

    }
}

data class SignUpData(
    val name: String,
    val username: String,
    val password: String,
    val email: String,
    val age: String,
    val gender: String,
    val agreedToTerms: Boolean
)

@Preview
@Composable
fun SignupPagePreview() {
    thirdactivity()
}