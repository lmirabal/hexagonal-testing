package lmirabal.bank.data

import lmirabal.bank.model.BankAccount

interface BankAccountRepository {
    fun add(account: BankAccount)
    fun update(account: BankAccount)
    fun list(): List<BankAccount>
}