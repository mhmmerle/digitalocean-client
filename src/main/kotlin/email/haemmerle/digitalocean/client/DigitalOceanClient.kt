package email.haemmerle.digitalocean.client

import com.beust.klaxon.Klaxon
import email.haemmerle.digitalocean.client.model.*
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

    fun createKubernetesCluster(cluster: KubernetesCluster) : KubernetesCluster {
        return jsonClient
                .performJsonPostRequest<KubernetesClusterResponse>(
                        "/v2/kubernetes/clusters",
                        Klaxon().toJsonString(cluster))!!
                .kubernetes_cluster
    }

}
