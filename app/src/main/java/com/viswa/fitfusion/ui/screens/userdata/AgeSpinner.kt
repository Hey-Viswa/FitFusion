import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AgePickerScreen(onNextClicked: () -> Unit, onBackClicked: () -> Unit) {
    var selectedAge by remember { mutableStateOf<String?>("16") }

    val db = Firebase.firestore

    // Age Picker Section
    val values = remember { (16..65).map { it.toString() } }
    val valuesPickerState = rememberPickerState()
    val visibleItemsCount = 5
    val visibleItemsMiddle = (visibleItemsCount - 1) / 2
    val desiredIndex = 0 // Index of "16" in your values list

    val itemHeightDp = 62.dp
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeightDp.roundToPx() }
    val initialScrollOffsetPx = 0

    val listStartIndex = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % values.size - visibleItemsMiddle + desiredIndex - 1
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = listStartIndex,
        initialFirstVisibleItemScrollOffset = initialScrollOffsetPx
    )

    val boxHeight = itemHeightDp * visibleItemsCount

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
                text = "HOW OLD ARE YOU?",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This helps us create your personalized plan",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        // Age Picker Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight),
            contentAlignment = Alignment.Center
        ) {
            EnhancedPicker(
                state = valuesPickerState,
                items = values,
                visibleItemsCount = visibleItemsCount,
                textModifier = Modifier,
                textStyle = TextStyle(fontSize = 30.sp),
                listState = listState
            )

            // Two Green Bars Highlighting the Selected Item
            Divider(
                color = Color(0xFFBFFF00),
                thickness = 2.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.3f)
                    .offset(y = -itemHeightDp / 2) // Top green bar
            )
            Divider(
                color = Color(0xFFBFFF00),
                thickness = 2.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.3f)
                    .offset(y = itemHeightDp / 2) // Bottom green bar
            )
        }

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Button(
                shape = CircleShape,
                onClick = {
                    // Handle back action
                    onBackClicked()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBFFF00),
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            // Next Button
            Button(
                onClick = {
                    val age = valuesPickerState.selectedItem
                    if (age.isNotEmpty()) {
                        selectedAge = age
                        // Upload to Firestore
                        val userAge = hashMapOf(
                            "age" to age
                        )
                        db.collection("users")
                            .add(userAge)
                            .addOnSuccessListener { documentReference ->
                                // Handle success
                                println("DocumentSnapshot added with ID: ${documentReference.id}")
                                // Proceed to the next screen
                                onNextClicked()
                            }
                            .addOnFailureListener { e ->
                                // Handle failure
                                println("Error adding document: $e")
                                // Show an error message if needed
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBFFF00),
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Next", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnhancedPicker(
    items: List<String>,
    state: PickerState = rememberPickerState(),
    modifier: Modifier = Modifier,
    listState: LazyListState,
    startIndex: Int = 0,
    visibleItemsCount: Int = 5,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = Color(0xFFBFFF00)
) {
    val visibleItemsMiddle = (visibleItemsCount - 1) / 2
    val itemHeightDp = 62.dp // Ensure consistency

    fun getItem(index: Int) = items[index % items.size]

    // Initialize the selected item
    if (state.selectedItem.isEmpty()) {
        val initialIndex = listState.firstVisibleItemIndex + visibleItemsMiddle
        state.selectedItem = getItem(initialIndex)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
        ) {
            items(Int.MAX_VALUE) { index ->
                val item = getItem(index)
                val isSelected = item == state.selectedItem

                // Animations and styling
                val scale by animateFloatAsState(targetValue = if (isSelected) 1.8f else 0.8f)
                val color = if (isSelected) Color.White else Color.Gray
                val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                val itemAlpha by animateFloatAsState(targetValue = if (isSelected) 1.0f else 0.5f)

                // Item layout
                Box(
                    modifier = Modifier
                        .height(itemHeightDp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        style = textStyle.copy(
                            fontWeight = fontWeight,
                            color = color
                        ),
                        modifier = Modifier
                            .scale(scale)
                            .alpha(itemAlpha)
                    )
                }
            }
        }
    }
}

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}

@Preview
@Composable
fun PreviewAgePickerScreen() {
    AgePickerScreen(
        onNextClicked = { /* Handle next action */ },
        onBackClicked = { /* Handle back action */ }
    )
}
