package email.haemmerle.digitalocean.client.model

/**
 * Kubernetes Cluster
 */
class KubernetesClustersResponse(val kubernetes_clusters: List<KubernetesCluster>,
                                 val meta: KubernetesClustersMeta, val links: Links = NoLinks)

open class Links(val pages : Pages)
class Pages(val first: String, val prev: String, val next: String, val last: String)
object NoLinks : Links(Pages("", "", "", ""))
class KubernetesClustersMeta(val total: Int)
class KubernetesCluster(val id: String = "", val name: String, val region: String, val version: String,
                        val cluster_subnet: String = "", val service_subnet: String = "",
                        val ipv4: String = "", val endpoint: String = "",
                        val tags: List<String>, val node_pools: List<NodePool>? = null,
                        val maintenance_policy: MaintenancePolicy? = null,
                        val auto_upgrade: Boolean = false, val status: Status? = null,
                        val meta: KubernetesClustersMeta? = null, val links: Links = NoLinks)

class KubernetesClusterResponse(val kubernetes_cluster: KubernetesCluster)

class MaintenancePolicy (val start_time: String, val duration: String, val day: String)
class NodePool(val id: String = "", val name: String, val size: String, val count: Int,
               val tags: List<String>, val nodes: List<Node> = listOf())

class Node(val id: String, val name: String, val status: Status,
           val created_at : String, val updated_at: String)

class Status(val state : String, val message : String = "")