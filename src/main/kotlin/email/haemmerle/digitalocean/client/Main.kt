package email.haemmerle.digitalocean.client

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option

fun main(args: Array<String>) = CLI()
        .subcommands(Publish())
        .main(args)

class CLI : CliktCommand(name = "application-name") {
    override fun run() = Unit
}

class Publish : CliktCommand(help = "Publish and download a document") {

    val argument by argument(help = "Application sample argument")
    val option by option("-o", "--option", help = "Application sample option'").multiple()


    override fun run() {
        // What to do when this subcommand is run
    }
}
