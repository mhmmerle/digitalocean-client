package email.haemmerle.digitalocean.client.template

import email.haemmerle.appconfig.AppConfig
import email.haemmerle.digitalocean.client.httpclient.ServerProperties

data class ApplicationProperties (val server: ServerProperties)

val appProperties by lazy {
    AppConfig().withSystemProperties().withEnvironment().withJsonFile("application.json")
            .get<ApplicationProperties>()
}

