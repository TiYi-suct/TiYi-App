package com.tiyi.tiyi_app.data

import com.tiyi.tiyi_app.model.CommonResponseModel

interface TiYiPhotosRepository {
    suspend fun userLogin(): CommonResponseModel
}