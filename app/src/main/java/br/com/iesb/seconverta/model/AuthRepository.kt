package br.com.iesb.seconverta.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    fun firebaseSignInWithGoogle(
        googleAuthCredential: AuthCredential,
        authenticatedUserLiveData: MutableLiveData<User>
    ) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val isNewUser = authTask.result.additionalUserInfo?.isNewUser
                val currenUser = firebaseAuth.currentUser
                currenUser?.let {
                    authenticatedUserLiveData.value = User(
                        currenUser.uid,
                        currenUser.displayName,
                        currenUser.email,
                        isNewUser ?: false
                    )
                }
            }
        }
    }

    fun createUserInFirestoreIfNotExists(
        authenticatedUser: User,
        createdUserLiveData: MutableLiveData<User>
    ) {
        val rootRef = FirebaseFirestore.getInstance()
        val usersRef = rootRef.collection(User.REF_NAME)
        val uidRef = usersRef.document(authenticatedUser.uid)
        uidRef.get().addOnCompleteListener { uidTask ->
            if (uidTask.isSuccessful) {
                val document = uidTask.result
                if (!document.exists()) {
                    uidRef.set(authenticatedUser).addOnCompleteListener {
                        if (it.isSuccessful) {
                            createdUserLiveData.value = authenticatedUser
                        }
                    }
                } else {
                    createdUserLiveData.value = authenticatedUser
                }
            }
        }
    }
}