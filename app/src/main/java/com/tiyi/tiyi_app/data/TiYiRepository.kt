package com.tiyi.tiyi_app.data

import com.tiyi.tiyi_app.model.ResponseModel

interface TiYiPhotosRepository {
    suspend fun userLogin(): ResponseModel
}