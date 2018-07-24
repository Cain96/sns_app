package com.cain96.sns_kanri.Data.Sns

import java.io.Serializable

data class Sns(
    var id: Int,
    var name: String = "",
    var path: String? = ""
) : Serializable
