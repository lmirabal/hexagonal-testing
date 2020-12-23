package lmirabal.bank.http

import lmirabal.bank.Bank
import lmirabal.bank.BankService
import lmirabal.bank.data.InMemoryBankAccountRepository
import lmirabal.bank.model.BankAccount
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.routes

fun bankHttp() = bankHttp(BankService(InMemoryBankAccountRepository()))

internal const val BANK_ACCOUNTS_URL = "/bank/accounts"
internal val bankAccountLens = Body.auto<BankAccount>().toLens()
internal val bankAccountListLens = Body.auto<List<BankAccount>>().toLens()

internal fun bankHttp(bank: Bank): HttpHandler {
    return routes(
        BANK_ACCOUNTS_URL bind routes(
            POST to { Response(OK).with(bankAccountLens of bank.createAccount()) },
            GET to { Response(OK).with(bankAccountListLens of bank.listAccounts()) }
        )
    )
}