package email.haemmerle.digitalocean.client

import email.haemmerle.httpclient.JsonHttpClient
import io.mockk.every
import io.mockk.mockk
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
}