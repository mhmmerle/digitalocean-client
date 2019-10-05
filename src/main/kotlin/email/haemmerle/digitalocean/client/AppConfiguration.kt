package email.haemmerle.digitalocean.client

import com.beust.klaxon.Klaxon
import email.haemmerle.appconfig.AppKonfig
import email.haemmerle.digitalocean.client.appConfigFile.FILENAME
import java.io.File
import java.net.URL

data class AppConfiguration(val server: ServerProperties)
data class ServerProperties(val url: String, val token: String)

val appConfig by lazy {
    AppKonfig()
            .withSystemProperties()
            .withEnvironment()
            .withJsonFile(FILENAME)
            .get<AppConfiguration>()
}

object appConfigFile {
    const val FILENAME: String = "application.json"

    fun updateConnectionConfig(url: String, token: String) {
        URL(url)
        if (!token.matches(Regex("[a-z0-9]{64}"))) {
            throw TokenFormatException("Token must consist of 64 alphanumeric characters")
        }
        val currentConfig = read();
        val updatedConfig = if (currentConfig != null) {
            currentConfig.copy(server = ServerProperties(url, token))
        } else {
            AppConfiguration(ServerProperties(url, token))
        }
        write(updatedConfig)
    }

    class TokenFormatException(message: String) : Throwable(message)

    fun read(): AppConfiguration? {
        if (File(FILENAME).exists()) {
            return Klaxon().parse<AppConfiguration>(File(FILENAME))
        } else {
            return null
        }
    }

    fun write(config: AppConfiguration) {
        File("application.json").writeText(Klaxon().toJsonString(config))
    }
}