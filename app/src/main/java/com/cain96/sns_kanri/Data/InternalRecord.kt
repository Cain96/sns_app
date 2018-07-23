package com.cain96.sns_kanri.Data

import com.cain96.sns_kanri.Data.Sns.Sns
import java.io.Serializable
import java.util.Date

class InternalRecord(
    var date: Date = Date(),
    var hour: Int = 0,
    var minutes: Int = 0,
    var sns: Sns = Sns(id = 1)
) : Serializable {
    fun timeToText(): String? {
        return if (hour < 0 || minutes < 0) {
            null
        } else {
            "%1$02d : %2$02d".format(hour, minutes)
        }
    }
    fun time(): String? {
        return if (hour < 0 || minutes < 0) {
            null
        } else {
            "%1$02d:%2$02d:00".format(hour, minutes)
        }
    }
}
