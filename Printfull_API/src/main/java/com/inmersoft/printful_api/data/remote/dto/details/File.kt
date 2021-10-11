package com.inmersoft.printful_api.data.remote.dto.details

data class File(
    val created: Int,
    val dpi: Int,
    val filename: String,
    val hash: String,
    val height: Int,
    val id: Int,
    val mime_type: String,
    val preview_url: String,
    val size: Int,
    val status: String,
    val thumbnail_url: String,
    val type: String,
    val url: Any,
    val visible: Boolean,
    val width: Int
)