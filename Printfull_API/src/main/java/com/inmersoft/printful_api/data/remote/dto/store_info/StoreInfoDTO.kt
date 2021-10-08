package com.inmersoft.printful_api.data.remote.dto.store_info

import com.inmersoft.printful_api.domain.model.StoreInfo

data class StoreInfoDTO(
    val code: Int,
    val extra: List<Any>,
    val result: StoreResult
)

fun StoreInfoDTO.toStoreInfo(): StoreInfo {
    return StoreInfo(
        result = result
    )
}