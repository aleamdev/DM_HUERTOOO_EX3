package com.dewis.huertohogar.ui.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dewis.dm_huertohogar_ex3.data.datastore.SessionDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionDataStore(app)

    val email: StateFlow<String?> =
        session.email.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val token: StateFlow<String?> =
        session.token.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logout(onDone: () -> Unit) = viewModelScope.launch {
        session.setEmail(null)
        session.setToken(null)
        onDone()
    }
}
