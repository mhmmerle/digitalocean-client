package email.haemmerle.digitalocean.client

import com.beust.klaxon.Klaxon
import email.haemmerle.digitalocean.client.model.KubernetesCluster
import email.haemmerle.digitalocean.client.model.KubernetesClusterResponse
import email.haemmerle.digitalocean.client.model.NodePool
import email.haemmerle.restclient.BearerAuthorization
import email.haemmerle.restclient.JsonHttpClient
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DigitalOceanClientTest {

    @Test
    fun canGetOptions() {
        // prepare
        val jsonClientMock = mockk<JsonHttpClient>()
        every { jsonClientMock.performJsonGetRequest("/v2/kubernetes/options") } returns """
            {
                "options":{
                    "regions":[
                        {"name":"New York 1","slug":"nyc1"},
                        {"name":"Frankfurt 1","slug":"fra1"},
                        {"name":"San Francisco 2","slug":"sfo2"}
                    ],
                    "versions":[
                        {"slug":"1.15.3-do.2","kubernetes_version":"1.15.3"},
                        {"slug":"1.14.6-do.2","kubernetes_version":"1.14.6"},
                        {"slug":"1.13.10-do.2","kubernetes_version":"1.13.10"}
                    ],
                    "sizes":[
                        {"name":"s-1vcpu-2gb","slug":"s-1vcpu-2gb"},
                        {"name":"s-2vcpu-2gb","slug":"s-2vcpu-2gb"},
                        {"name":"s-2vcpu-4gb","slug":"s-2vcpu-4gb"}
                    ]
                }
            }
        """.toByteArray()
        val sut = DigitalOceanClient(jsonClientMock)

        // when
        val result = sut.getKubernetesOptions()

        // then
        assertThat(result.sizes.size).isEqualTo(3)
        assertThat(result.sizes[1].name).isEqualTo("s-2vcpu-2gb")
        assertThat(result.versions.size).isEqualTo(3)
        assertThat(result.regions.size).isEqualTo(3)
    }

    @Test
    fun canListEmptyClusters() {
        // prepare
        val jsonClientMock = mockk<JsonHttpClient>()
        every { jsonClientMock.performJsonGetRequest("/v2/kubernetes/clusters") } returns """
            {"kubernetes_clusters":[],"meta":{"total":0},"links":{}}
            """.toByteArray()
        val sut = DigitalOceanClient(jsonClientMock)

        // when
        val result = sut.getKubernetesClusters()

        // then
        assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun canListClusters() {
        // prepare
        val jsonClientMock = mockk<JsonHttpClient>()
        every {
            jsonClientMock.performJsonGetRequest("/v2/kubernetes/clusters")
        } returns """
            {
              "kubernetes_clusters": [
                {
                  "id": "bd5f5959-5e1e-4205-a714-a914373942af",
                  "name": "prod-cluster-01",
                  "region": "nyc1",
                  "version": "1.14.1-do.4",
                  "cluster_subnet": "10.244.0.0/16",
                  "service_subnet": "10.245.0.0/16",
                  "ipv4": "68.183.121.157",
                  "endpoint": "https://bd5f5959-5e1e-4205-a714-a914373942af.k8s.ondigitalocean.com",
                  "tags": [
                    "production",
                    "web-team",
                    "k8s",
                    "k8s:bd5f5959-5e1e-4205-a714-a914373942af"
                  ],
                  "node_pools": [
                    {
                      "id": "cdda885e-7663-40c8-bc74-3a036c66545d",
                      "name": "frontend-pool",
                      "size": "s-1vcpu-2gb",
                      "count": 3,
                      "tags": [
                        "production",
                        "web-team",
                        "k8s",
                        "k8s:bd5f5959-5e1e-4205-a714-a914373942af",
                        "k8s:worker"
                      ],
                      "nodes": [
                        {
                          "id": "478247f8-b1bb-4f7a-8db9-2a5f8d4b8f8f",
                          "name": "adoring-newton-3niq",
                          "status": {
                            "state": "provisioning"
                          },
                          "created_at": "2018-11-15T16:00:11Z",
                          "updated_at": "2018-11-15T16:00:11Z"
                        },
                        {
                          "id": "ad12e744-c2a9-473d-8aa9-be5680500eb1",
                          "name": "adoring-newton-3nim",
                          "status": {
                            "state": "provisioning"
                          },
                          "created_at": "2018-11-15T16:00:11Z",
                          "updated_at": "2018-11-15T16:00:11Z"
                        },
                        {
                          "id": "e46e8d07-f58f-4ff1-9737-97246364400e",
                          "name": "adoring-newton-3ni7",
                          "status": {
                            "state": "provisioning"
                          },
                          "created_at": "2018-11-15T16:00:11Z",
                          "updated_at": "2018-11-15T16:00:11Z"
                        }
                      ]
                    },
                    {
                      "id": "f49f4379-7e7f-4af5-aeb6-0354bd840778",
                      "name": "backend-pool",
                      "size": "c-4",
                      "count": 2,
                      "tags": [
                        "production",
                        "web-team",
                        "k8s",
                        "k8s:bd5f5959-5e1e-4205-a714-a914373942af",
                        "k8s:worker"
                      ],
                      "nodes": [
                        {
                          "id": "3385619f-8ec3-42ba-bb23-8d21b8ba7518",
                          "name": "affectionate-nightingale-3nif",
                          "status": {
                            "state": "provisioning"
                          },
                          "created_at": "2018-11-15T16:00:11Z",
                          "updated_at": "2018-11-15T16:00:11Z"
                        },
                        {
                          "id": "4b8f60ff-ba06-4523-a6a4-b8148244c7e6",
                          "name": "affectionate-nightingale-3niy",
                          "status": {
                            "state": "provisioning"
                          },
                          "created_at": "2018-11-15T16:00:11Z",
                          "updated_at": "2018-11-15T16:00:11Z"
                        }
                      ]
                    }
                  ],
                  "maintenance_policy": {
                    "start_time": "00:00",
                    "duration": "4h0m0s",
                    "day": "any"
                  },
                  "auto_upgrade": false,
                  "status": {
                    "state": "provisioning",
                    "message": "provisioning"
                  },
                  "created_at": "2018-11-15T16:00:11Z",
                  "updated_at": "2018-11-15T16:00:11Z"
                }
              ],
              "meta": {
                "total": 2
              }
            }
        """.toByteArray()
        val sut = DigitalOceanClient(jsonClientMock)

        // when
        val result = sut.getKubernetesClusters()

        // then
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].node_pools!!.size).isEqualTo(2)
        assertThat(result[0].id).isEqualTo("bd5f5959-5e1e-4205-a714-a914373942af")
    }

    @Test
    fun canCreateKubernetesCluster() {
        // prepare
        val jsonClientMock = mockk<JsonHttpClient>()
        every {
            jsonClientMock.performJsonPostRequest<KubernetesClusterResponse>("/v2/kubernetes/cluster", any())
        } returns Klaxon().parse("""
            "kubernetes_cluster": {
            "id": "fbd7dba7-c452-4b34-ba77-81bca226c273",
            "name": "test-cluster-01",
            "region": "fra1",
            "version": "1.15.3-do.3",
            "cluster_subnet": "10.244.0.0/16",
            "service_subnet": "10.245.0.0/16",
            "vpc_uuid": "",
            "ipv4": "",
            "endpoint": "",
            "tags": [
              "test",
              "k8s",
              "k8s:fbd7dba7-c452-4b34-ba77-81bca226c273"
            ],
            "node_pools": [
              {
                "id": "262b7fbe-4c98-48c3-9c6f-c839eaa28795",
                "name": "test",
                "size": "s-1vcpu-2gb",
                "count": 3,
                "tags": [
                  "test",
                  "k8s",
                  "k8s:fbd7dba7-c452-4b34-ba77-81bca226c273",
                  "k8s:worker"
                ],
                "auto_scale": false,
                "min_nodes": 0,
                "max_nodes": 0,
                "nodes": [
                  {
                    "id": "5c26b3c5-09d3-4f19-a498-aae74c3fd095",
                    "name": "",
                    "status": {
                      "state": "provisioning"
                    },
                    "droplet_id": "",
                    "created_at": "2019-10-05T15:42:16Z",
                    "updated_at": "2019-10-05T15:42:16Z"
                  }
                ]
              }
            ],
            "maintenance_policy": {
              "start_time": "13:00",
              "duration": "4h0m0s",
              "day": "any"
            },
            "auto_upgrade": false,
            "status": {
              "state": "provisioning",
              "message": "provisioning"
            },
            "created_at": "2019-10-05T15:42:16Z",
            "updated_at": "2019-10-05T15:42:16Z"
          }
        }
        """)
        val sut = DigitalOceanClient(jsonClientMock)
        val desiredState = KubernetesCluster(
                name = "test-cluster-01", region = "fra1", version = "1.15.3-do.3", tags = listOf("a-cluster-tag"),
                node_pools = listOf( NodePool(size = "s-1vcpu-2gb", count = 1, name = "test-pool", tags = listOf("a-pool-tag"))))

        // when
        sut.createKubernetesCluster(desiredState)

        // then
        verify { jsonClientMock.performJsonPostRequest("/v2/kubernetes/clusters", Klaxon().toJsonString(desiredState)) }
    }

    @Test
    fun getDigitalOceanResponse() {
        // prepare
        val sut = JsonHttpClient(appConfig.server.url, BearerAuthorization(appConfig.server.token))

        // when
        val response = sut.performJsonPostRequest("/v2/kubernetes/clusters", """{
          "name": "test-cluster-01",
          "region": "fra1",
          "version": "1.15.3-do.3",
          "tags": [
            "test"
          ],
          "node_pools": [
            {
              "size": "s-1vcpu-2gb",
              "count": 3,
              "name": "test",
              "tags": [
                "test"
              ]
            }
          ]
        }""")

        // then
        println(response.toString(Charsets.UTF_8))
    }
}