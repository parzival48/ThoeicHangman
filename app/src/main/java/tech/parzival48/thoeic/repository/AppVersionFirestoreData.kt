package tech.parzival48.thoeic.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import timber.log.Timber

class AppVersionFirestoreData(databaseService: FirebaseFirestore) : LiveData<Int>() {

    private var registration: ListenerRegistration? = null
    private val docReference = databaseService.collection("api").document("version")

    override fun onActive() {
        super.onActive()
        registration = docReference.addSnapshotListener(DocEventListener())
    }

    override fun onInactive() {
        super.onInactive()
        if (!hasActiveObservers()) {
            registration?.remove()
            registration = null
        }
    }

    private inner class DocEventListener : EventListener<DocumentSnapshot> {
        override fun onEvent(snapshot: DocumentSnapshot?, error: FirebaseFirestoreException?) {
            error?.let {
                Timber.d(it)
            }
            snapshot?.let {
                value = it.getLong("code")?.toInt()
            }
        }
    }


    /*
    suspend fun getVersionName(): String = withContext(Dispatchers.Main) {
        val versionName: String
        databaseService.document("ver")
    }
    /*
     */
    {
        withContext(Dispatchers.Main) {
            databaseService.document("version").get()
                .addOnSuccessListener { result ->
                    result.getString("name")?.let{
                        versionName = it
                    }
                }
                .addOnFailureListener {
                    versionName = BuildConfig.VERSION_NAME
                }
            return@withContext versionName
        }
    }
    */

}