package com.example.healthlens3

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

@Composable
fun firstactivity(onLoginClick: () -> Unit, onSignUpClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.health_lens_logo),
            contentDescription = "Health Lens Logo",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Welcome to Health Lens",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Text(
            text = "Your health companion for accurate disease prediction",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { onLoginClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Button(
                onClick = { onSignUpClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }
            val database: DatabaseReference = Firebase.database.reference
            val myRef: DatabaseReference = database.child("message")

            Button(
                onClick = {
                    myRef.setValue("Hello, World!")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Set Value in Firebase")
            }
        }
    }

}


@Preview
@Composable
fun app()
{
    firstactivity(onLoginClick = { /*TODO*/ }) {
    }
}