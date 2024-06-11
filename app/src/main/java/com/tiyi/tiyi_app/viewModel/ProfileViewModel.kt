package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.dto.RechargeItemsModel
import com.tiyi.tiyi_app.dto.UserDetailsModel
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    private val _userDetails = MutableStateFlow<UserDetailsModel.UserDetails?>(null)
    val userDetails: StateFlow<UserDetailsModel.UserDetails?> get() = _userDetails
    private val _rechargeItems =
        MutableStateFlow<List<RechargeItemsModel.RechargeItem>>(emptyList())
    val rechargeItems: StateFlow<List<RechargeItemsModel.RechargeItem>> = _rechargeItems

    init {
        fetchUserDetails()
        loadRechargeItems()
    }

     fun fetchUserDetails() {
        Log.d(TAG, "fetchUserDetails: ")
        viewModelScope.launch {
            val fakeUserDetails = UserDetailsModel.UserDetails(
                username = "John Wick",
                avatar = "",
                musicCoin = 100,
                signature = "I am fine thank you"
            )

            _userDetails.value = fakeUserDetails
        }
//        viewModelScope.launch {
//            try {
//                val userDetails = networkRepository.getUserDetails()
//                _userDetails.value = userDetails
//            } catch (e: Exception) {
//                // Handle the error, e.g., log it or show a message to the user
//                e.printStackTrace()
//            }
//        }
    }

    private fun loadRechargeItems() {
        Log.d(TAG, "loadRechargeItems: ")
        viewModelScope.launch {
            when (val result = networkRepository.getRechargeItems()) {
                is Result.Success -> {
                    val response = result.data
                    _rechargeItems.value = response.data
                }

                else -> {
                    Log.d(TAG, "获取充值项异常")
                }
            }
        }

//        viewModelScope.launch {
//            try {
//                val response = networkRepository.getRechargeItems()
//                if (response.code == 200) {
//                    _rechargeItems.value = response.data
//                } else {
//                    // handle error case
//                }
//            } catch (e: Exception) {
//                // handle exception
//            }
//        }
    }

    fun createOrder(rechargeId: Int, onSuccess: (String) -> Unit, onError: () -> Unit) {
        Log.d(TAG, "createOrder: $rechargeId")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = networkRepository.getOrderInfo(rechargeId)) {
                is Result.Success -> {
                    val response = result.data
                    Log.d("test", response.data)
                    onSuccess(response.data)
                    fetchUserDetails()
                }

                else -> {
                    onError()
                }
            }
        }


//        viewModelScope.launch {
//            try {
//                val response = networkRepository.getOrderInfo(rechargeId)
//                if (response.code == 200) {
//                    onSuccess()
//                } else {
//                    onError()
//                }
//            } catch (e: Exception) {
//                onError()
//            }
//        }
    }
}