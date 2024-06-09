package com.tiyi.tiyi_app.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.data.NetworkRepository
import com.tiyi.tiyi_app.dto.UserDetailsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val networkRepository: NetworkRepository) : ViewModel() {

    private val _userDetails = MutableStateFlow<UserDetailsModel?>(null)
    val userDetails: StateFlow<UserDetailsModel?> get() = _userDetails

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        viewModelScope.launch {
            try {
                val userDetails = networkRepository.getUserDetails()
                _userDetails.value = userDetails
            } catch (e: Exception) {
                // Handle the error, e.g., log it or show a message to the user
                e.printStackTrace()
            }
        }
    }
}