package lmirabal.bank.web

import lmirabal.bank.Bank
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.SEE_OTHER
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.long
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel
import java.math.BigDecimal
import java.util.UUID

fun bankWeb(bank: Bank): HttpHandler {
    return routes(
        "/" bind routes(
            POST to CreateAccount(bank),
            GET to ListAccounts(bank)
        ),
        "/{id}/deposit" bind POST to ChangeBalance(bank) { id, amount -> deposit(id, amount) },
        "/{id}/withdraw" bind POST to ChangeBalance(bank) { id, amount -> withdraw(id, amount) },
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

    operator fun invoke(bank: Bank): HttpHandler = {
        val accounts = bank.listAccounts().map { account -> account.toViewModel() }
        Response(Status.OK).with(viewLens of BankAccountListView(accounts))
    }

    private fun BankAccount.toViewModel() = BankAccountView(id.value.toString(), balance.format())

}

class ChangeBalance(private val bank: Bank, private val action: Bank.(BankAccountId, Amount) -> BankAccount) :
    HttpHandler {
    private val amountField = FormField.long().map { majorUnits -> majorUnits.toAmount() }.required("amount")
    private val formBody = Body.webForm(Validator.Feedback, amountField).toLens()

    override fun invoke(request: Request): Response {
        val id = request.path("id") ?: throw Exception("Bank account id must be present")
        val form = formBody(request)
        val amount = amountField(form)
        action(bank, BankAccountId(UUID.fromString(id)), amount)
        return Response(SEE_OTHER).header("location", "/")
    }

    private fun Long.toAmount() = Amount(
        BigDecimal(this)
            .movePointRight(2)
            .longValueExact()
    )
}

fun Amount.format(): String {
    return BigDecimal(minorUnits).movePointLeft(2).longValueExact().toString()
}

data class BankAccountView(val id: String, val balance: String)
data class BankAccountListView(val accounts: List<BankAccountView>) : ViewModel
