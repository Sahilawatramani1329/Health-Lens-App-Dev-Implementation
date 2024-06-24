import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlens3.ui.theme.Healthlens3Theme
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.tasks.await
import timber.log.Timber


data class User(
    val name: String = "",
    val age: String = "",
    val email: String = ""
)

data class DiseasePrediction(
    val diseaseName: String = "",
    val date: String = ""
)

@Composable
fun fourthactivity(userId: String, onUploadClick: () -> Unit) {
    val database = FirebaseDatabase.getInstance()

    var userInfo by remember { mutableStateOf<User?>(null) }
    var userDiseaseHistory by remember { mutableStateOf<List<DiseasePrediction>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        try {
            val userRef = database.getReference("users").child(userId)
            val userSnapshot = userRef.get().await()
            userInfo = userSnapshot.getValue(User::class.java)

            val historyRef = database.getReference("diseaseHistory").child(userId)
            val historySnapshot = historyRef.get().await()
            val history = historySnapshot.children.mapNotNull { it.getValue(DiseasePrediction::class.java) }
            userDiseaseHistory = history

            loading = false
        } catch (e: Exception) {
            Timber.e(e, "Error fetching data")
            loading = false
        }
    }

    Healthlens3Theme {
        Surface(color = Color(0xFFFFFFFF), modifier = Modifier.fillMaxSize()) {
            if (loading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "Loading...", fontSize = 24.sp, color = Color.Gray)
                }
            } else {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = com.example.healthlens3.R.drawable.ic_profile),
                        contentDescription = "Health Lens Logo",
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                    userInfo?.let { user ->
                        Text(text = "User Profile", fontSize = 24.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Name: ${user.name}, Age: ${user.age}", fontSize = 18.sp, color = Color.Black)
                        Text(text = "Email: ${user.email}", fontSize = 18.sp, color = Color.Black)
                    }

                    if (userDiseaseHistory.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Disease Prediction History:", fontSize = 20.sp, color = Color.Black)
                        LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                            items(userDiseaseHistory) { history ->
                                DiseaseHistoryRow(history)
                            }
                        }
                    }

                    Button(onClick = onUploadClick) {
                        Text(text = "Upload New Image")
                    }
                }
            }
        }
    }
}

@Composable
fun DiseaseHistoryRow(prediction: DiseasePrediction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = prediction.diseaseName,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = prediction.date,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
