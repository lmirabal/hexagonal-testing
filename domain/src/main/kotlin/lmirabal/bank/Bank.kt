package lmirabal.bank

import dev.forkhandles.result4k.Result
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import lmirabal.bank.model.NotEnoughFunds

interface Bank {
    fun createAccount(): BankAccount
    fun listAccounts(): List<BankAccount>
    fun deposit(id: BankAccountId, amount: Amount): BankAccount
    fun withdraw(id: BankAccountId, amount: Amount): Result<BankAccount, NotEnoughFunds>
}