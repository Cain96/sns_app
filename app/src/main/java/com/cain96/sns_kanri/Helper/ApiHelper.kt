package com.cain96.sns_kanri.Helper

import android.util.Log
import com.cain96.sns_kanri.Data.InternalRecord
import com.cain96.sns_kanri.Data.Record.Records
import com.cain96.sns_kanri.Data.Record.RecordsDeserializer
import com.cain96.sns_kanri.Data.Sns.Sns
import com.cain96.sns_kanri.Data.Sns.SnsListDeserializer
import com.cain96.sns_kanri.Data.Statistics.Statistics
import com.cain96.sns_kanri.Data.Statistics.StatisticsDeserializer
import com.cain96.sns_kanri.Data.Statistics.Time
import com.cain96.sns_kanri.Utils.toString
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.fuel.moshi.responseObject
import com.github.kittinunf.result.Result
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat
import java.util.Date

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
        val (request, _, result) = async {
            return@async "/sns/".httpGet().responseObject(SnsListDeserializer())
        }.await()
        when (result) {
            is Result.Success -> {
                val (sns_list, _) = result
                return sns_list
            }
            is Result.Failure -> {
                Log.d("SNS", "fail")
                Log.d("SNS", request.cUrlString())
            }
        }
        return null
    }

    suspend fun requestAllSns(): List<Sns>? {
        val (request, _, result) = async {
            return@async "/sns/".httpGet(
                listOf(
                    "all" to "true"
                )
            ).responseObject(SnsListDeserializer())
        }.await()
        when (result) {
            is Result.Success -> {
                val (sns_list, _) = result
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
        val (request, _, result) = async {
            return@async "/sns/%s".format(id).httpGet().responseObject<Sns>()
        }.await()
        when (result) {
            is Result.Success -> {
                val (sns, _) = result
                return sns
            }
            is Result.Failure -> {
                Log.d("SNS", "fail")
                Log.d("SNS", request.cUrlString())
            }
        }
        return null
    }

    suspend fun createSns(sns: Sns): Boolean {
        val body: String = """{
            "name": "${sns.name}",
            "color": "${sns.color}",
            "enabled": "${sns.enabled}"
        }""".trimIndent()

        val (request, _, result) = async {
            return@async "/sns/".httpPost().body(body).responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                return true
            }
            is Result.Failure -> {
                Log.d("Sns", "fail")
                Log.d("Sns", request.cUrlString())
                return false
            }
        }
    }

    suspend fun editSns(sns: Sns): Boolean {
        val body: String = """{
            "name": "${sns.name}",
            "color": "${sns.color}",
            "enabled": ${sns.enabled}
        }""".trimIndent()

        val (request, _, result) = async {
            return@async "/sns/${sns.id}/".httpPut().body(body).responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                return true
            }
            is Result.Failure -> {
                Log.d("Sns", "fail")
                Log.d("Sns", request.cUrlString())
                return false
            }
        }
    }

    suspend fun requestRecords(): Records? {
        val (request, _, result) = async {
            return@async "/output/".httpGet().responseObject(RecordsDeserializer())
        }.await()
        when (result) {
            is Result.Success -> {
                val (records, _) = result
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
        val df = SimpleDateFormat("yyyy-MM-dd")
        val body: String = """{
            "sns": ${record.sns?.id},
            "date": "${df.format(record.date)}",
            "time": "${record.time()}"
        }""".trimIndent()

        val (request, _, result) = async {
            return@async "/record/".httpPost().body(body).responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                return true
            }
            is Result.Failure -> {
                Log.d("Record", "fail")
                Log.d("Record", request.cUrlString())
                return false
            }
        }
    }

    suspend fun deleteRecord(id: Int): Boolean {
        val (request, _, result) = async {
            return@async "/record/$id/".httpDelete().responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                return true
            }
            is Result.Failure -> {
                Log.d("Record", "fail")
                Log.d("Record", request.cUrlString())
                return false
            }
        }
    }

    suspend fun editRecord(record: InternalRecord): Boolean {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val body: String = """{
            "sns": ${record.sns?.id},
            "date": "${df.format(record.date)}",
            "time": "${record.time()}"
        }""".trimIndent()

        val (request, _, result) = async {
            return@async "/record/${record.id}/".httpPut().body(body).responseJson()
        }.await()

        when (result) {
            is Result.Success -> {
                return true
            }
            is Result.Failure -> {
                Log.d("Record", "fail")
                Log.d("Record", request.cUrlString())
                return false
            }
        }
    }

    suspend fun requestStatistics(start_date: Date, end_date: Date): Statistics? {
        val (request, _, result) = async {
            return@async "/statistic/".httpGet(
                listOf(
                    "date_0" to start_date.toString("yyyy-MM-dd"),
                    "date_1" to end_date.toString("yyyy-MM-dd")
                )
            ).responseObject(StatisticsDeserializer())
        }.await()
        when (result) {
            is Result.Success -> {
                val (statistics, _) = result
                return statistics
            }
            is Result.Failure -> {
                Log.d("Statistics", "fail")
                Log.d("Statistics", request.cUrlString())
            }
        }
        return null
    }

    suspend fun requestTime(): Time? {
        val (request, _, result) = async {
            return@async "/time/".httpGet().responseObject<Time>()
        }.await()
        when (result) {
            is Result.Success -> {
                val (time, _) = result
                return time
            }
            is Result.Failure -> {
                Log.d("Time", "fail")
                Log.d("Time", request.cUrlString())
            }
        }
        return null
    }
}
