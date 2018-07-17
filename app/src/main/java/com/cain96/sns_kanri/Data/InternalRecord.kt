package com.cain96.sns_kanri.Data

import com.cain96.sns_kanri.Data.Sns.Sns
import java.io.Serializable
import java.util.Date

class InternalRecord(
    var date: Date = Date(),
    var hour: Int = 0,
    var minutes: Int = 0,
    var sns: Sns = Sns(id = 1)
) : Serializable
