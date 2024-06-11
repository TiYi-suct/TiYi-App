package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import kotlinx.coroutines.withContext
import java.net.URL

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
            when (val result = networkRepository.getUserDetails()) {
                is Result.Success -> {
                    val response = result.data
                    _userDetails.value = response.data
                }

                else -> {
                    Log.e(TAG, "获取用户信息失败")
                }
            }
        }
    }

    // TODO 更换头像
    fun updateAvatar(
        /*TODO*/
    ) {
        /*TODO*/
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
    }
}