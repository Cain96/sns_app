package com.cain96.sns_kanri.Data.Statistics

import com.cain96.sns_kanri.Utils.DateAdapter
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class StatisticsDeserializer : ResponseDeserializable<Statistics> {
    override fun deserialize(content: String): Statistics? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).add(DateAdapter()).build()
        val statisticAdapter: JsonAdapter<Statistics> = moshi.adapter(Statistics::class.java)
        return statisticAdapter.fromJson(content)
    }
}
