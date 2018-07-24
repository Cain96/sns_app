package com.cain96.sns_kanri.Helper

import android.util.Log
import com.cain96.sns_kanri.Data.InternalRecord
import com.cain96.sns_kanri.Data.Record.Records
import com.cain96.sns_kanri.Data.Record.RecordsDeserializer
import com.cain96.sns_kanri.Data.Sns.Sns
import com.cain96.sns_kanri.Data.Sns.SnsListDeserializer
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.responseObject
import com.github.kittinunf.result.Result
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat

class ApiHelper {
    var manager: FuelManager = FuelManager.instance

    companion object {
        fun createInstance(): ApiHelper {
            val apiHelper = ApiHelper()
            apiHelper.manager.basePath = "https://calm-stream-98493.herokuapp.com/api"
            apiHelper.manager.baseHeaders = mapOf("Content-Type" to "application/json")
            return apiHelper
        }
    }

    suspend fun requestLogin(email: String = "admin@sample.com", password: String = "hogefuga") {
        Log.d("login", "start")
        val body: String = """{
            "email" : "$email",
            "password": "$password"
        }""".trimIndent()

        val (request, _, result) = async {
            return@async "/login/".httpPost().body(body).responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                Log.d("login", "success")
                val jwt_token = result.value.obj().get("token") as String
                this.manager.baseHeaders = mapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to "JWT $jwt_token"
                )
            }
            is Result.Failure -> {
                Log.d("login", "fail")
                Log.d("login", request.cUrlString())
            }
        }
    }

    suspend fun requestSns(): List<Sns>? {
        Log.d("SNS", "start")
        val (request, _, result) = async {
            return@async "/sns/".httpGet().responseObject(SnsListDeserializer())
        }.await()
        when (result) {
            is Result.Success -> {
                Log.d("SNS", "success")
                val (sns_list, err) = result
                return sns_list
            }
            is Result.Failure -> {
                Log.d("SNS", "fail")
                Log.d("SNS", request.cUrlString())
            }
        }
        return null
    }

    suspend fun getSns(id: Int): Sns? {
        Log.d("SNS", "start")
        val (request, _, result) = async {
            return@async "/sns/%s".format(id).httpGet().responseObject<Sns>()
        }.await()
        when (result) {
            is Result.Success -> {
                Log.d("SNS", "success")
                val (sns, err) = result
                return sns
            }
            is Result.Failure -> {
                Log.d("SNS", "fail")
                Log.d("SNS", request.cUrlString())
            }
        }
        return null
    }

    suspend fun requestRecords(): Records? {
        Log.d("Record", "start")
        val (request, _, result) = async {
            return@async "/record/".httpGet().responseObject(RecordsDeserializer())
        }.await()
        when (result) {
            is Result.Success -> {
                Log.d("Record", "success")
                val (records, err) = result
                return records
            }
            is Result.Failure -> {
                Log.d("Record", "fail")
                Log.d("Record", request.cUrlString())
            }
        }
        return null
    }

    suspend fun createRecord(record: InternalRecord): Boolean {
        Log.d("Record", "start")
        val df = SimpleDateFormat("yyyy-MM-dd")
        val body: String = """{
            "sns": ${record.sns.id},
            "date": "${df.format(record.date)}",
            "time": "${record.time()}"
        }""".trimIndent()

        val (request, _, result) = async {
            return@async "/record/".httpPost().body(body).responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                Log.d("Record", "success")
                return true
            }
            is Result.Failure -> {
                Log.d("Record", "fail")
                Log.d("Record", request.cUrlString())
                return false
            }
        }
    }
}
