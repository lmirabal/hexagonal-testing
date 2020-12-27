package lmirabal.bank

import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId

interface Bank {
    fun createAccount(): BankAccount
    fun listAccounts(): List<BankAccount>
    fun deposit(id: BankAccountId, amount: Amount): BankAccount
}