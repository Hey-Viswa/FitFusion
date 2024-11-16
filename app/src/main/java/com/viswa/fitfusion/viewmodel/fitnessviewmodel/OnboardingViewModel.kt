import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OnboardingViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Coroutine function to upload the gender and return a success flag.
    suspend fun uploadGender(selectedGender: String): Boolean {
        return userRepository.uploadGender(selectedGender)
    }

    // Set onboarding completion
    fun completeOnboarding() {
        viewModelScope.launch {
            userRepository.setOnboardingComplete(true)
        }
    }
}
