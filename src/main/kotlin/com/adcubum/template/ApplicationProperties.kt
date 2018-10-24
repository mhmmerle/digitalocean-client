package com.adcubum.template

import com.adcubum.appconfig.AppConfig
import com.adcubum.httpclient.ServerProperties

data class ApplicationProperties (val server: ServerProperties)

val appProperties by lazy {
    AppConfig().withSystemProperties().withEnvironment().withJsonFile("application.json")
            .get<ApplicationProperties>()
}

