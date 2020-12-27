package lmirabal.bank.http

import lmirabal.bank.Bank
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.with

class BankHttpClient(val http: HttpHandler) : Bank {
    override fun createAccount(): BankAccount {
        val response = http(Request(POST, BANK_ACCOUNTS_BASE_URL))
        return bankAccountLens(response)
    }

    override fun listAccounts(): List<BankAccount> {
        val response = http(Request(GET, BANK_ACCOUNTS_BASE_URL))
        return bankAccountListLens(response)
    }

    override fun deposit(id: BankAccountId, amount: Amount): BankAccount {
        val response = http(
            Request(POST, BANK_ACCOUNTS_BASE_URL + BANK_ACCOUNT_DEPOSIT_PATH)
                .with(accountIdLens of id, amountLens of amount)
        )
        return bankAccountLens(response)
    }
}