package br.com.iesb.seconverta.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User constructor(
    var uid: String,
    var name: String? = "",
    var email: String? = "",
    @Exclude var isNew: Boolean = false
) : Serializable {
    companion object {
        const val REF_NAME = "users"
    }

}