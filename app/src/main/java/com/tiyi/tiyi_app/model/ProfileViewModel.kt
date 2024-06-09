package com.tiyi.tiyi_app.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.data.NetworkRepository
import com.tiyi.tiyi_app.dto.RechargeItemsModel
import com.tiyi.tiyi_app.dto.UserDetailsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {
    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _userDetails = MutableStateFlow<UserDetailsModel.UserDetails?>(null)
    val userDetails: StateFlow<UserDetailsModel.UserDetails?> get() = _userDetails
    private val _rechargeItems =
        MutableStateFlow<List<RechargeItemsModel.RechargeItem>>(emptyList())
    val rechargeItems: StateFlow<List<RechargeItemsModel.RechargeItem>> = _rechargeItems

    init {
//        fetchUserDetails()
    }

    private fun fetchUserDetails() {
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

    fun loadRechargeItems() {
        Log.d(TAG, "loadRechargeItems: ")
        viewModelScope.launch {
            // 模拟返回假数据
            val fakeRechargeItems = listOf(
                RechargeItemsModel.RechargeItem(
                    id = 1,
                    title = "10 Tokens",
                    amount = 10,
                    price = 1
                ),
                RechargeItemsModel.RechargeItem(
                    id = 2,
                    title = "50 Tokens",
                    amount = 50,
                    price = 5
                ),
                RechargeItemsModel.RechargeItem(
                    id = 3,
                    title = "100 Tokens",
                    amount = 100,
                    price = 10
                )
            )
            _rechargeItems.value = fakeRechargeItems
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

    fun createOrder(rechargeId: String, onSuccess: () -> Unit, onError: () -> Unit) {
        Log.d(TAG, "createOrder: $rechargeId")

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