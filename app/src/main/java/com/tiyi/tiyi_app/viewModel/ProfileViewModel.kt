package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.application.TokenManager
import com.tiyi.tiyi_app.dto.RechargeItemsModel
import com.tiyi.tiyi_app.dto.UserDetailsModel
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

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

    fun updateAvatar(
        newAvatarUri: Uri
    ) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val bitmap =
                BitmapFactory.decodeStream(context.contentResolver.openInputStream(newAvatarUri))
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val requestBody = RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                byteArrayOutputStream.toByteArray()
            )
            val body = MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestBody)
            when (val result = networkRepository.updateAvatar(body)) {
                is Result.Success -> {
                    val response = result.data
                    _userDetails.value = _userDetails.value?.copy(avatar = response.data)
                }

                is Result.BadRequest -> {
                    Log.e(TAG, result.message)
                }

                else -> {
                    Log.e(TAG, result.toString())
                    Log.e(TAG, "更新头像失败")
                }
            }
        }
    }

    fun editSignature(newSignature: String) {
        viewModelScope.launch {
            when (networkRepository.editSignature(newSignature)) {
                is Result.Success -> {
                    fetchUserDetails()
                }

                else -> {
                    Log.e(TAG, "编辑个性签名失败")
                }
            }
        }
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

    fun logout() {
        Log.d(TAG, "logout: ")
        TokenManager.clearToken()
    }
}