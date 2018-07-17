package com.cain96.sns_kanri.Data.Sns

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SnsListDeserializer : ResponseDeserializable<List<Sns>> {
    override fun deserialize(content: String): List<Sns>? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, Sns::class.java)
        val snsListAdapter: JsonAdapter<List<Sns>> = moshi.adapter(type)
        return snsListAdapter.fromJson(content)
    }
}
