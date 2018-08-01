package com.cain96.sns_kanri.Data.Record

import com.cain96.sns_kanri.Data.Sns.Sns
import java.util.Date

data class Record(
    val id: Int,
    val sns: Sns,
    val date: Date,
    val time: Date,
    val updated: Date
)
