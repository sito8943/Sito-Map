package com.inmersoft.printful_api.data.remote.dto.products

data class Paging(
    val limit: Int,
    val offset: Int,
    val total: Int
)