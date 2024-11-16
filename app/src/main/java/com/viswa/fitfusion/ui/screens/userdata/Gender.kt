import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viswa.fitfusion.R
import kotlinx.coroutines.launch
@Composable
fun GenderSelectionScreen(
    onNextClicked: () -> Unit,
    onboardingViewModel: OnboardingViewModel,
    onGenderSelected: (String) -> Unit
) {
    var selectedGender by remember { mutableStateOf<String?>("Male") }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "TELL US ABOUT YOURSELF!",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "To Give You A Better Experience We Need To Know Your Gender",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        // Gender Selection Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GenderButton(
                gender = "Male",
                isSelected = selectedGender == "Male",
                icon = R.drawable.ic_male,
                onClick = { selectedGender = "Male" }
            )
            Spacer(modifier = Modifier.height(24.dp))
            GenderButton(
                gender = "Female",
                isSelected = selectedGender == "Female",
                icon = R.drawable.ic_female,
                onClick = { selectedGender = "Female" }
            )
        }

        // Next Button with Loading Indicator
        Button(
            onClick = {
                selectedGender?.let { gender ->
                    isUploading = true
                    uploadError = null

                    // Call onGenderSelected and pass the selected gender
                    coroutineScope.launch {
                        onGenderSelected(gender)
                        isUploading = false
                    }
                }
            },
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBFFF00),
                contentColor = Color.Black
            ),
            enabled = selectedGender != null && !isUploading
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Next", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Display Error Message if Upload Fails
        uploadError?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}


@Composable
fun GenderButton(gender: String, isSelected: Boolean, icon: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .background(
                color = if (isSelected) Color(0xFFBFFF00) else Color.DarkGray,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = if (isSelected) Color.Black else Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gender,
                color = if (isSelected) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
