package lmirabal.bank.data

import lmirabal.bank.model.BankAccount

class InMemoryBankAccountRepository : BankAccountRepository {
    private val accounts: MutableList<BankAccount> = arrayListOf()

    override fun add(account: BankAccount) {
        accounts.add(account)
    }

    override fun list(): List<BankAccount> = accounts
}