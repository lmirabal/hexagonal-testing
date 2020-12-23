package lmirabal.bank.web

import lmirabal.bank.Bank
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.SEE_OTHER
import org.http4k.core.with
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel
import java.math.BigDecimal

fun bankWeb(bank: Bank): HttpHandler {
    return routes(
        "/" bind routes(
            POST to CreateAccount(bank),
            GET to ListAccounts(bank)
        )
    )
}

object CreateAccount {
    operator fun invoke(bank: Bank): HttpHandler = {
        bank.createAccount()
        Response(SEE_OTHER).header("location", "/")
    }
}

object ListAccounts {
    private val templates = HandlebarsTemplates().CachingClasspath()
    private val viewLens = Body.viewModel(templates, TEXT_HTML).toLens()

    operator fun invoke(client: Bank): HttpHandler = {
        val accounts = client.listAccounts().map { account -> account.toViewModel() }
        Response(Status.OK).with(viewLens of BankAccountListView(accounts))
    }

    private fun BankAccount.toViewModel() = BankAccountView(id.value.toString(), balance.format())

    private fun Amount.format() = BigDecimal(minorUnits).movePointLeft(2).toPlainString()
}

data class BankAccountView(val id: String, val balance: String)
data class BankAccountListView(val accounts: List<BankAccountView>) : ViewModel
