package email.haemmerle.digitalocean.client

import email.haemmerle.httpclient.BearerAuthorization
import email.haemmerle.httpclient.JsonHttpClient
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
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
    fun canListClusters() {
        // prepare
        val jsonClientMock = mockk<JsonHttpClient>()
        every { jsonClientMock.performJsonGetRequest("/v2/kubernetes/clusters") } returns """
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
        assertThat(result[0].node_pools.size).isEqualTo(2)
        assertThat(result[0].id).isEqualTo("bd5f5959-5e1e-4205-a714-a914373942af")
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
    @Ignore
    fun getDigitalOceanResponse() {
        // prepare
        val sut = JsonHttpClient(
                "https://api.digitalocean.com",
                BearerAuthorization("0f9d1898ab3740c2f4c4e284122e911ace79464dd56bebeb045baddf064f51f0"))

        // when
        val response = sut.performJsonGetRequest("/v2/kubernetes/clusters")

        // then
        println(response.toString(Charsets.UTF_8))
    }
}