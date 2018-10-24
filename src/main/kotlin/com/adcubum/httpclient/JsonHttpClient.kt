package com.adcubum.httpclient;

import com.adcubum.template.*
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.apache.logging.log4j.LogManager
import java.nio.charset.StandardCharsets


class JsonHttpClient (val hostBaseUrl : String, val username : String, val password : String) {

    companion object {
        private val logger = LogManager.getLogger()
        private val cookieRegex: Regex = """(.+)="(.+)".+""".toRegex()

        private const val CONTENTTYPE_JSON_UTF8 = "application/json;charset=utf-8"
        private const val ENCODING_GZIP_DEFLATE = "gzip, deflate"
        private const val ONE_MINUTE_IN_MILISECONDS = 60000
    }

    class Headers {
        companion object {
            const val CONTENT_TYPE = "Content-Type"
            const val COOKIE = "Cookie"
            const val ACCEPT = "Accept"
            const val ACCEPT_ENCODING = "Accept-Encoding"
            const val SET_COOKIE = "Set-Cookie"
        }
    }

    val loginCookie: Pair<String, String> by lazy {
        Headers.COOKIE to login()
    }


    fun login(): String {
        val (_, response, result) = "${hostBaseUrl}/authenticate"
                .httpPost(listOf("username" to username, "password" to password))
                .response()
        handleFailure(response, result)
        return extractCookie(response)
    }

    private fun handleFailure(response: Response, result: Result<ByteArray, FuelError>) {
        if (response.statusCode == -1) {
            throw ServerUnreachable("$hostBaseUrl ${response.url}")
        }
        if (response.statusCode == 500) {
            throw UnexpectedServerError("Check Server ($hostBaseUrl) log for further information.")
        }
        if (result is Result.Failure) {
            if (response.headers[Headers.CONTENT_TYPE]?.get(0)?.replace(" ", "") == CONTENTTYPE_JSON_UTF8) {
                val ResponseObject = Klaxon().parse<ResponseObject>(response.data.toString(StandardCharsets.UTF_8))
                throw HttpRequestFailed("Request to ${response.url} responds: ${ResponseObject?.errorText().orEmpty()}")
            } else {
                throw HttpRequestFailed("Request to ${response.url} responds: ${response.data.toString(StandardCharsets.UTF_8)}")
            }
        }
    }

    private fun extractCookie(response: Response): String {
        val cookies = response.headers[Headers.SET_COOKIE]?.map { cookie ->
            cookieRegex.matchEntire(cookie)!!.destructured
        } ?: throw LoginFailed();
        return cookies.map { (name, value) -> "$name=\"$value\"" }.joinToString(separator = ";")
    }

    fun performJsonRequest(path: String, body: String = ""): ByteArray {
        return if (body.isEmpty()) performJsonGetRequest(path) else performJsonPostRequest(path, body)
    }

    fun performJsonGetRequest(path: String): ByteArray {
        val (_, response, result) = "${hostBaseUrl}${path}".httpGet()
                .header(Headers.ACCEPT to CONTENTTYPE_JSON_UTF8)
                .header(Headers.ACCEPT_ENCODING to ENCODING_GZIP_DEFLATE)
                .header(loginCookie)
                .timeout(ONE_MINUTE_IN_MILISECONDS).response()
        handleFailure(response, result)
        return result.get()
    }

    fun performJsonPostRequest(path: String, body: String = ""): ByteArray {
        val request = "${appProperties.server.url}${path}".httpPost()
                .header(Headers.ACCEPT to CONTENTTYPE_JSON_UTF8)
                .header(Headers.ACCEPT_ENCODING to ENCODING_GZIP_DEFLATE)
                .header(loginCookie)
                .timeout(ONE_MINUTE_IN_MILISECONDS)
        if (body.isNotEmpty()) {
            request.body(body)
            request.headers[Headers.CONTENT_TYPE] = CONTENTTYPE_JSON_UTF8
        }
        val (_, response, result) = request.response()
        handleFailure(response, result)
        return result.get()
    }
}

class LoginFailed : Throwable()

class UnexpectedServerError(message: String) : Throwable(message)

class ServerUnreachable(url: String) : Throwable(url)

class HttpRequestFailed(message: String) : Throwable(message)