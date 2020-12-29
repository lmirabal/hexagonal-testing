package lmirabal.bank

import lmirabal.bank.data.BankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId

class BankService(
    private val accountRepository: BankAccountRepository,
    private val idFactory: () -> BankAccountId = { BankAccountId.random() }
) : Bank {
    override fun createAccount(): BankAccount {
        return BankAccount(idFactory(), Amount.ZERO)
            .also { newAccount -> accountRepository.add(newAccount) }
    }

    override fun listAccounts(): List<BankAccount> = accountRepository.list()

    override fun deposit(id: BankAccountId, amount: Amount): BankAccount {
        val account = accountRepository.list().first { it.id == id }
        return account.deposit(amount)
            .also { updatedAccount -> accountRepository.update(updatedAccount) }
    }

    override fun withdraw(id: BankAccountId, amount: Amount): BankAccount {
        val account = accountRepository.list().first { it.id == id }
        return account.withdraw(amount)
            .also { updatedAccount -> accountRepository.update(updatedAccount) }
    }
}
