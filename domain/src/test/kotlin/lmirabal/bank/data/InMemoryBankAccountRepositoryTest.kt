package lmirabal.bank.data

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.junit.jupiter.api.Test

class InMemoryBankAccountRepositoryTest {
    private val repository = InMemoryBankAccountRepository()

    @Test
    fun persistsAccounts() {
        val account1 = BankAccount(BankAccountId.random(), Amount(10))
        repository.add(account1)
        val account2 = BankAccount(BankAccountId.random(), Amount(20))
        repository.add(account2)

        assertThat(repository.list(), equalTo(listOf(account1, account2)))
    }
}