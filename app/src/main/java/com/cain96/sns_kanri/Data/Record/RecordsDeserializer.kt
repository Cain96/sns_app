package com.cain96.sns_kanri.Data.Record

import com.cain96.sns_kanri.Utils.DateAdapter
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class RecordsDeserializer : ResponseDeserializable<Records> {
    override fun deserialize(content: String): Records? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).add(DateAdapter()).build()
        val recordsAdapter: JsonAdapter<Records> = moshi.adapter(Records::class.java)
        return recordsAdapter.fromJson(content)
    }
}
