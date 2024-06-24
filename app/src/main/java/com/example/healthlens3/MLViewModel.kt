import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthlens3.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.IOException

class MLViewModel : ViewModel() {
    private val _predictionResult = MutableLiveData<String>()
    val predictionResult: LiveData<String> = _predictionResult

    fun predictImage(imageUri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = FileUtil.from(context, imageUri) // You need to implement FileUtil.from to get a File from a Uri
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull()))
                .build()

            val request = Request.Builder()
                .url("http://3.110.196.103/") // Make sure to point to the correct endpoint
                .post(requestBody)
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    _predictionResult.postValue("Error: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        responseBody?.let {
                            // Assuming the response JSON is of the format {"class": "className", "probability": probabilityValue}
                            val jsonObject = JSONObject(it)
                            val className = jsonObject.getString("class")
                            _predictionResult.postValue("Class: $className")
                        }
                    } else {
                        _predictionResult.postValue("Error: ${response.message}")
                    }
                }
            })
        }
    }
}
