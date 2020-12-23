package lmirabal.bank.http

import lmirabal.bank.Bank
import lmirabal.bank.model.BankAccount
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request

class BankHttpClient(val http: HttpHandler) : Bank {
    override fun createAccount(): BankAccount {
        val response = http(Request(POST, BANK_ACCOUNTS_URL))
        return bankAccountLens(response)
    }

    override fun listAccounts(): List<BankAccount> {
        val response = http(Request(GET, BANK_ACCOUNTS_URL))
        return bankAccountListLens(response)
    }
}