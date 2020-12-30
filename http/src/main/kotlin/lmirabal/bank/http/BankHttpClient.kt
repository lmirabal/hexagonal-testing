package lmirabal.bank.http

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import lmirabal.bank.Bank
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import lmirabal.bank.model.NotEnoughFunds
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
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
        val response = changeBalanceAction(BANK_ACCOUNT_DEPOSIT_PATH, id, amount)
        return bankAccountLens(response)
    }

    override fun withdraw(id: BankAccountId, amount: Amount): Result<BankAccount, NotEnoughFunds> {
        val response = changeBalanceAction(BANK_ACCOUNT_WITHDRAWAL_PATH, id, amount)
        return when (response.status) {
            Status.OK -> Success(bankAccountLens(response))
            Status.BAD_REQUEST -> Failure(notEnoughFundsLens(response))
            else -> throw Exception("Not expected: $response")
        }
    }

    private fun changeBalanceAction(actionPath: String, id: BankAccountId, amount: Amount): Response {
        return http(
            Request(POST, BANK_ACCOUNTS_BASE_URL + actionPath)
                .with(accountIdLens of id, amountLens of amount)
        )
    }
}