import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.viswa.fitfusion.data.repository.DataStoreManager
import kotlinx.coroutines.tasks.await

class UserRepository(private val dataStoreManager: DataStoreManager) {

    suspend fun uploadGender(selectedGender: String): Boolean {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        val userGender = hashMapOf("gender" to selectedGender)

        return try {
            if (currentUser != null) {
                val userId = currentUser.uid
                db.collection("users").document(userId).set(userGender).await()
            } else {
                db.collection("users").add(userGender).await()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun setOnboardingComplete(isComplete: Boolean) {
        dataStoreManager.setOnboardingComplete(isComplete)
    }
}
