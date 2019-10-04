package email.haemmerle.digitalocean.client

import email.haemmerle.digitalocean.client.model.KubernetesCluster
import email.haemmerle.digitalocean.client.model.KubernetesClustersResponse
import email.haemmerle.digitalocean.client.model.KubernetesOptions
import email.haemmerle.digitalocean.client.model.KubernetesOptionsResponse
import email.haemmerle.restclient.BearerAuthorization
import email.haemmerle.restclient.JsonHttpClient

class DigitalOceanClient {
    val jsonClient: JsonHttpClient

    constructor(jsonClient: JsonHttpClient) {
        this.jsonClient = jsonClient
    }

    constructor(baseUrl: String, bearer: String) {
        this.jsonClient = JsonHttpClient(baseUrl, BearerAuthorization(bearer))
    }

    fun getKubernetesOptions(): KubernetesOptions {
        return jsonClient
                .performJsonGetRequest<KubernetesOptionsResponse>("/v2/kubernetes/options")!!
                .options
    }

    fun getKubernetesClusters(): List<KubernetesCluster> {
        return jsonClient
                .performJsonGetRequest<KubernetesClustersResponse>("/v2/kubernetes/clusters")!!
                .kubernetes_clusters
    }

}
