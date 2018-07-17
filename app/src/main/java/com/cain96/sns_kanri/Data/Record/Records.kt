package com.cain96.sns_kanri.Data.Record

data class Records(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Record>?
)
