package lmirabal.bank.http

import lmirabal.bank.Bank
import lmirabal.bank.BankService
import lmirabal.bank.data.InMemoryBankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Path
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.util.UUID

fun bankHttp() = bankHttp(BankService(InMemoryBankAccountRepository()))

internal const val BANK_ACCOUNTS_BASE_URL = "/bank/accounts"
internal const val BANK_ACCOUNT_DEPOSIT_PATH = "/{id}/deposit"
internal val bankAccountLens = Body.auto<BankAccount>().toLens()
internal val bankAccountListLens = Body.auto<List<BankAccount>>().toLens()
internal val accountIdLens = Path.map({ BankAccountId(UUID.fromString(it)) }, { it.value.toString() }).of("id")
internal val amountLens = Body.auto<Amount>().toLens()

internal fun bankHttp(bank: Bank): HttpHandler {
    return routes(
        BANK_ACCOUNTS_BASE_URL bind routes(
            routes(
                POST to { Response(OK).with(bankAccountLens of bank.createAccount()) },
                GET to { Response(OK).with(bankAccountListLens of bank.listAccounts()) }
            ),
            BANK_ACCOUNT_DEPOSIT_PATH bind POST to { request ->
                val accountId = accountIdLens(request)
                val amount = amountLens(request)

                Response(OK).with(bankAccountLens of bank.deposit(accountId, amount))
            }
        ),
    )
}