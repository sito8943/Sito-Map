package com.inmersoft.printful_api.data.remote.dto.details

import com.inmersoft.printful_api.domain.model.Details

data class DetailsDTO(
    val code: Int,
    val extra: List<Any>,
    val result: DetailsResult
)

fun DetailsDTO.toDetails(): Details {
    return Details(
        result = result
    )
}