package email.haemmerle.digitalocean.client

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt

fun main(args: Array<String>) = DigitalOcean().subcommands(
        Kubernetes().subcommands(
                Options()
        ),
        Configure().subcommands(
                Connection()
        )).main(args)

class DigitalOcean : CliktCommand(name = "do", help = "Use the Digital Ocean API from command line") {
    override fun run() = Unit
}

class Kubernetes : CliktCommand(help = "Manage Kubernetes clusters in Digital Ocean") {
    override fun run() = Unit
}

class Options : CliktCommand(help = "Show available options for Kubernetes clusters") {
    override fun run() {
        val options = DigitalOceanClient(appConfig.server.url, appConfig.server.token).getKubernetesOptions()
        println("REGIONS")
        options.regions.forEach { println("${it.slug}: ${it.name}") }
        println("NODE SIZES")
        options.sizes.forEach { println("${it.slug}: ${it.name}")  }
        println("KUBERNETES VERSIONS")
        options.versions.forEach { println("${it.slug}: ${it.kubernetes_version}")  }
    }
}

class Configure : CliktCommand(help = "Configure settings") {
    override fun run() = Unit
}

class Connection : CliktCommand(help = "Configure the connection to Digital Ocean API server") {

    companion object {
        const val URL_HELP = "Base url of the Digital Ocean API server"
        const val TOKEN_HELP = "API token for authorization"
    }

    val url by option(help = URL_HELP).prompt(default = "https://api.digitalocean.com", text = URL_HELP)
    val token by option(help = TOKEN_HELP).prompt(text = TOKEN_HELP)

    override fun run() {
        appConfigFile.updateConnectionConfig(url, token)
    }

}