package com.cain96.sns_kanri.Data.Sns

import java.io.Serializable

data class Sns(
    var id: Int = 0,
    var name: String = "",
    var color: String? = ""
) : Serializable
