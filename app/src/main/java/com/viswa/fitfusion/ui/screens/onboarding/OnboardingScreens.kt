import android.R.attr.animationDuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.LayoutDirection

class SlantedShape(
    private val slantHeight: Float = 200f // Increase this value for a more pronounced slant
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): Outline {
        val path = Path().apply {
            // Define the points to create a more slanted edge
            moveTo(0f, 0f)  // Top-left corner
            lineTo(size.width, 0f)  // Top-right corner
            lineTo(size.width, size.height - slantHeight)  // More pronounced slant at bottom-right corner
            lineTo(0f, size.height)  // Bottom-left corner
            close()  // Close the path to connect back to the starting point
        }
        return Outline.Generic(path)
    }
}


@Composable
fun OnboardingScreen(
    navController: NavController,
    titleFontSize: Float = 28f,
    descriptionFontSize: Float = 18f,
    buttonStyle: @Composable (text: String, onClick: () -> Unit) -> Unit = { text, onClick ->
        Button(onClick = onClick) {
            Text(text = text)
        }
    }
) {
    val client = remember {
        HttpClient(Android) {
            engine {
                connectTimeout = 10_000
                socketTimeout = 10_000
            }
        }
    }

    val imageCache = remember { mutableMapOf<String, ImageBitmap>() }
    val imageUrls = getAdjustedImageUrls()

    var currentPage by remember { mutableStateOf(0) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(currentPage) {
        imageBitmap = loadImageFromUrl(client, imageUrls[currentPage], imageCache)
        if (currentPage + 1 < imageUrls.size) {
            val nextUrl = imageUrls[currentPage + 1]
            if (!imageCache.containsKey(nextUrl)) {
                loadImageFromUrl(client, nextUrl, imageCache)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Set black background for better visual contrast
    ) {
        // Full Width and Edge-to-Edge Image with Slanted Cut
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "Fitness App Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth() // Fill the entire width of the screen
                    .height(450.dp) // Increased height to make the image more impactful
                    .clip(SlantedShape(slantHeight = 150f)), // Slanted bottom to match the increased height
                contentScale = ContentScale.Crop // Makes sure the image fills width and crops the excess
            )
        } else {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp) // Increased height to match the actual image
                    .clip(SlantedShape(slantHeight = 150f)), // Keep the slanted bottom consistent
                contentScale = ContentScale.Crop
            )
        }

        // Text and Buttons Below the Slanted Image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = 300f // Adjusted gradient end to smoothly blend with the new height
                    )
                )
                .padding(16.dp), // Added minimal padding for content alignment
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Keep this minimal to reduce space between elements

            Text(
                text = when (currentPage) {
                    0 -> "MEET YOUR COACH,"
                    1 -> "TRACK YOUR PROGRESS,"
                    2 -> "ACHIEVE YOUR GOALS"
                    else -> ""
                },
                fontSize = titleFontSize.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp)) // Reduced spacer height to minimize gap

            Text(
                text = when (currentPage) {
                    0 -> "Track your workouts, monitor your progress, and achieve your fitness goals!"
                    1 -> "Easily monitor your daily, weekly, and monthly progress to stay motivated."
                    2 -> "Set personalized goals and achieve them with our guided workout plans!"
                    else -> ""
                },
                fontSize = descriptionFontSize.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spacer before the PageIndicator

            PageIndicatorStyle(
                currentPage = currentPage,
                pageCount = imageUrls.size,
                modifier = Modifier.padding(vertical = 8.dp) // Adjust padding to bring the indicator closer
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacer before the PageIndicator
            // Row containing buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Ensure vertical alignment is centered
            ) {
                if (currentPage > 0) {
                    CustomTextButton(
                        text = "Back",
                        onClick = { currentPage-- },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp) // Add some padding to align nicely with the main button
                    )
                } else {
                    Spacer(modifier = Modifier.width(72.dp)) // Space placeholder to align with the "Next" button
                }

                if (currentPage < imageUrls.size - 1) {
                    CustomButton(
                        text = "Next",
                        onClick = { currentPage++ },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } else {
                    CustomButton(
                        text = "Get Started",
                        onClick = {
                            navController.navigate("workout") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = Color(0xFFBFFF00), // Bright green color by default
    textColor: Color = Color.Black, // Default text color is black
    cornerRadius: Dp = 25.dp // Default corner radius
) {
    // Animation state for button press
    var isPressed by remember { mutableStateOf(false) }

    // Animate scale between 1f (normal) and 1.1f (slightly larger)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200) // Duration of the animation
    )

    Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .height(50.dp) // Set a fixed height for consistency
            .width(200.dp) // Set a fixed width to ensure consistency across different screens
            .clip(RoundedCornerShape(cornerRadius)) // Rounded corners for a modern look
            .scale(scale) // Apply scaling animation to the button
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        // When pressed, change the state to true and trigger the animation
                        isPressed = true
                        tryAwaitRelease() // Wait until the gesture is finished
                        isPressed = false // Reset the state after the user releases the press
                    }
                )
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor // Use the button color parameter
        ),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = text,
            color = textColor, // Use the text color parameter
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1 // Ensure the text does not wrap into multiple lines
        )
    }
}

@Composable
fun CustomTextButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color = Color(0xffbfff00), // Default text color for text button
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    )
}





@Composable
fun PageIndicatorStyle(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    dotWidth: Dp = 30.dp, // Wider width for flat indicators
    dotHeight: Dp = 4.dp, // Shorter height for flat indicators
    animationDuration: Int = 500,
    selectedColor: Color = Color(0xFFBFFF00), // Greenish highlight color for the selected indicator
    unselectedColor: Color = Color.DarkGray // Dark gray for the rest of the indicators
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until pageCount) {
            val isSelected = i == currentPage

            val dotColor by animateColorAsState(
                targetValue = if (isSelected) selectedColor else unselectedColor,
                animationSpec = tween(durationMillis = animationDuration)
            )

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.4f else 1f,
                animationSpec = tween(durationMillis = animationDuration)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(dotWidth * scale) // Scale width to emphasize selected indicator
                    .height(dotHeight) // Fixed height to keep it "flat"
                    .background(dotColor, shape = RoundedCornerShape(percent = 50)) // Rounded corners for a pill-like shape
            )
        }
    }
}




// Function to load the image from a URL using Ktor with caching
suspend fun loadImageFromUrl(
    client: HttpClient,
    url: String,
    imageCache: MutableMap<String, ImageBitmap>
): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        // Check if the image is already in the cache
        imageCache[url]?.let { return@withContext it }

        // Fetch the image data
        val response: HttpResponse = client.get(url)
        val inputStream = response.bodyAsChannel().toInputStream()
        val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
        val imageBitmap = bitmap?.asImageBitmap()

        // Cache the image for future use
        if (imageBitmap != null) {
            imageCache[url] = imageBitmap
        }

        imageBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun getAdjustedImageUrls(): List<String> {
    val configuration = LocalConfiguration.current
    val screenWidth =
        configuration.screenWidthDp * configuration.densityDpi / 160 // Screen width in pixels
    val screenHeight =
        configuration.screenHeightDp * configuration.densityDpi / 160 // Screen height in pixels

    // Adjust width and height based on screen dimensions
    val width = screenWidth.coerceAtLeast(1000)
    val height = screenHeight.coerceAtLeast(1000)

    return listOf(
        "https://images.pexels.com/photos/4752861/pexels-photo-4752861.jpeg?w=$width&h=$height",
        "https://images.pexels.com/photos/4164772/pexels-photo-4164772.jpeg?w=$width&h=$height",
        "https://images.pexels.com/photos/4164761/pexels-photo-4164761.jpeg?w=$width&h=$height"
    )
}




