package email.haemmerle.digitalocean.client

import email.haemmerle.appconfig.AppConfig

data class ApplicationProperties (val server: ServerProperties)

val appProperties by lazy {
    AppConfig().withSystemProperties().withEnvironment().withJsonFile("application.json")
            .get<ApplicationProperties>()
}

