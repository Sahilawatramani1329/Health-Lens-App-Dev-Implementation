package com.example.healthlens3

import MLViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun fifthactivity() {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val mlViewModel: MLViewModel = viewModel()
    val predictionResult by mlViewModel.predictionResult.observeAsState("")

    // Activity result launcher for picking an image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri.value = it
            mlViewModel.predictImage(uri, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Upload an Image", modifier = Modifier.padding(vertical = 16.dp))

        Button(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Select from Gallery")
        }

        // Display the prediction result when it's available
        if (predictionResult.isNotEmpty()) {
            Text(
                text = "Prediction Result: $predictionResult",
                modifier = Modifier.padding(top = 32.dp)
            )

            // Check if the prediction result contains "Actinic keratosis"
            when {
                predictionResult.contains("Actinic keratosis") -> DisplayActinicKeratosisInfo()
                predictionResult.contains("Basal cell carcinoma") -> DisplayBasalCellCarcinomaInfo()
                predictionResult.contains("Benign keratosis") -> DisplayBenignKeratosisInfo()
                predictionResult.contains("Dermatofibroma") -> DisplayDermatofibromaInfo()
                predictionResult.contains("Melanocytic nevus") -> DisplayMelanocyticNevusInfo()
                predictionResult.contains("Melanoma") -> DisplayMelanomaInfo()
                predictionResult.contains("Squamous cell carcinoma") -> DisplaySquamousCellCarcinomaInfo()
                predictionResult.contains("Vascular lesion") -> DisplayVascularLesionInfo()
                // Add as many conditions as needed
            }
        }
    }
}

// Composable function to display details about Actinic keratosis
@Composable
fun DisplayActinicKeratosisInfo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Actinic keratosis is a common skin condition characterized by rough, scaly patches that typically develop on sun-exposed areas such as the face, scalp, and hands. These patches may vary in color from pink to brown and have a rough texture resembling sandpaper. Protecting the skin from excessive sun exposure by wearing sunscreen with a high SPF, seeking shade, and wearing protective clothing like wide-brimmed hats and long sleeves can help prevent the development of actinic keratosis. Treatments for actinic keratosis include 5-Fluorouracil cream, Imiquimod cream, and Photodynamic therapy, all of which are designed to remove the damaged cells or stimulate the body's immune system to attack the abnormal cells.",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun DisplayDermatofibromaInfo() {
    Text(text = "Dermatofibroma is a benign skin growth that appears as small, firm bumps often found on the legs. These growths are usually brownish in color and may dimple or indent when pinched. While dermatofibromas are harmless and typically don't require treatment, options for those seeking removal for cosmetic reasons include cryotherapy, laser treatment, and surgical excision to remove the growths and improve skin texture.", modifier = Modifier.padding(top = 16.dp))
}

@Composable
fun DisplayBasalCellCarcinomaInfo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Basal cell carcinoma, the most common type of skin cancer, often appears as pearly or waxy bumps, or as flat, flesh-colored or brown lesions on sun-exposed areas like the face, neck, and ears. These growths may bleed easily, ooze, or crust over. For effective treatment and mitigation, early detection through regular skin checks is essential. Treatments include surgical excision, Mohs surgery, and topical Imiquimod, aimed at removing the cancerous cells while minimizing damage to the surrounding healthy tissue.",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun DisplayBenignKeratosisInfo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Benign keratosis, also known as seborrheic keratosis, presents as benign, wart-like growths or thickened patches on the skin. These growths are typically non-cancerous and can vary in color from light tan to black. While benign keratoses are harmless, they can be cosmetically bothersome. Treatments such as cryotherapy, laser therapy, and curettage can effectively remove these growths, improving skin appearance.",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun DisplayMelanocyticNevusInfo() {
    Text(text = "Melanocytic nevus, commonly known as moles, are growths on the skin that are usually brown or black and can appear anywhere on the body. Most moles are benign, but changes in size, shape, or color may indicate malignancy, such as melanoma. Regular self-examinations and professional skin checks are vital. For atypical moles or those at risk of becoming cancerous, treatments include surgical excision, laser removal, and cryotherapy to prevent potential malignancy.", modifier = Modifier.padding(top = 16.dp))
}

@Composable
fun DisplayMelanomaInfo() {
    Text(text = "Melanoma is a serious form of skin cancer that develops from melanocytes, the pigment-producing cells in the skin. It often appears as an abnormal mole or lesion with irregular borders, asymmetrical shape, and varied colors. Early detection is crucial for effective treatment. Treatments for melanoma include surgical excision, immunotherapy (e.g., Ipilimumab), and targeted therapy (e.g., BRAF inhibitors), which are selected based on the stage and characteristics of the melanoma.", modifier = Modifier.padding(top = 16.dp))
}

@Composable
fun DisplaySquamousCellCarcinomaInfo() {
    Text(text = "Squamous cell carcinoma is a type of skin cancer that often presents as red, scaly patches or open sores on sun-exposed areas like the face, ears, neck, and hands. Treatments for squamous cell carcinoma include surgical excision, radiation therapy, and topical chemotherapy (e.g., 5-Fluorouracil cream), which aim to eradicate cancer cells and reduce the risk of recurrence.", modifier = Modifier.padding(top = 16.dp))
}

@Composable
fun DisplayVascularLesionInfo() {
    Text(text = "Vascular lesion encompasses various skin conditions characterized by abnormal blood vessels, such as hemangiomas or port-wine stains. Treatment options for vascular lesions include laser therapy (e.g., pulsed dye laser), surgical removal, and beta-blockers (for certain hemangiomas), depending on the type and severity of the lesion. Consulting a dermatologist for proper evaluation and management is recommended to determine the most suitable treatment approach.", modifier = Modifier.padding(top = 16.dp))
}
