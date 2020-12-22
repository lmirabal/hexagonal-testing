package lmirabal.bank

import lmirabal.bank.model.BankAccount

interface Bank {
    fun createAccount(): BankAccount
    fun listAccounts(): List<BankAccount>
}