package email.haemmerle.digitalocean.client.model

/**
 * Kubernetes Options
 */
class KubernetesOptionsResponse(val options: KubernetesOptions)

class KubernetesOptions(val sizes: List<Size>, val versions: List<Version>, val regions: List<Region>)
class Size(val name: String, val slug: String)
class Version(val kubernetes_version: String, val slug: String)
class Region(val name: String, val slug: String)