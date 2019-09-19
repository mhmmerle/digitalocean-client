package email.haemmerle.digitalocean.client

import email.haemmerle.httpclient.BearerAuthorization
import email.haemmerle.httpclient.JsonHttpClient

class DigitalOceanClient {
    val jsonClient : JsonHttpClient

    constructor(jsonClient: JsonHttpClient) {
        this.jsonClient = jsonClient
    }

    constructor(baseUrl : String, bearer : String) {
        this.jsonClient = JsonHttpClient(baseUrl, BearerAuthorization(bearer))
    }

    fun getKubernetesOptions() : KubernetesOptions {
        return jsonClient
                .performJsonGetRequest<KubernetesOptionsResponse>("/v2/kubernetes/options")!!
                .options
    }

}

class KubernetesOptionsResponse(val options : KubernetesOptions)
class KubernetesOptions(val sizes : List<Size>, val versions: List<Version>, val regions : List<Region>)
class Size(val name : String, val slug : String)
class Version(val kubernetes_version : String, val slug : String)
class Region(val name : String, val slug : String)