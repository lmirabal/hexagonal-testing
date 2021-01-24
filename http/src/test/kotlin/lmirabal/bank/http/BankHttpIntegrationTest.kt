package lmirabal.bank.http

import lmirabal.bank.BankContract
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class BankHttpIntegrationTest : BankContract() {
    private val server = bankHttp().asServer(SunHttp())
    override val bank = BankHttpClient(
        SetBaseUriFrom(Uri.of("http://localhost:${server.port()}")).then(JavaHttpClient())
    )

    @BeforeEach
    fun setUp() {
        server.start()
    }

    @AfterEach
    fun tearDown() {
        server.stop()
    }
}