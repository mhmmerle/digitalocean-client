package email.haemmerle.digitalocean.client

import email.haemmerle.httpclient.BearerAuthorization
import email.haemmerle.httpclient.JsonHttpClient
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.*

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

class KubernetesClustersResponse(val kubernetes_clusters: List<KubernetesCluster>,
                                 val meta: KubernetesClustersMeta, val links: Links = NoLinks )
open class Links(val pages : Pages)
class Pages(val first: String, val prev: String, val next: String, val last: String)
object NoLinks : Links(Pages("", "", "", ""))
class KubernetesClustersMeta(val total: Int)
class KubernetesCluster(val id: String, val name: String, val region: String, val version: String,
                        val cluster_subnet: String, val service_subnet: String, val ipv4: String, val endpoint: String,
                        val tags: List<String>, val node_pools: List<NodePool>,
                        val maintenance_policy: MaintenancePolicy, val auto_upgrade: Boolean, val status: Status)
class MaintenancePolicy (val start_time: String, val duration: String, val day: String)
class NodePool(val id: String, val name: String, val size: String, val count: Int,
               val tags: List<String>, val nodes: List<Node>)
class Node(val id: String, val name: String, val status: Status,
           val created_at : String, val updated_at: String)
class Status(val state : String, val message : String = "")

class KubernetesOptionsResponse(val options: KubernetesOptions)
class KubernetesOptions(val sizes: List<Size>, val versions: List<Version>, val regions: List<Region>)
class Size(val name: String, val slug: String)
class Version(val kubernetes_version: String, val slug: String)
class Region(val name: String, val slug: String)