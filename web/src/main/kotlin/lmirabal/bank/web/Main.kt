package lmirabal.bank.web

import lmirabal.bank.http.BankHttpClient
import lmirabal.bank.http.bankHttp
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val http = bankHttp().asServer(SunHttp(8080))
    val httpClient = BankHttpClient(
        SetBaseUriFrom(Uri.of("http://localhost:${http.port()}")).then(JavaHttpClient())
    )
    val app = bankWeb(httpClient).asServer(SunHttp(80))
    http.start()
    app.start()
}