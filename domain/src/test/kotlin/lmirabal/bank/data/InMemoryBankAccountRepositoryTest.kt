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
    fun `persists accounts`() {
        val account1 = BankAccount(BankAccountId.random(), Amount(10))
        repository.add(account1)
        val account2 = BankAccount(BankAccountId.random(), Amount(20))
        repository.add(account2)

        assertThat(repository.list(), equalTo(listOf(account1, account2)))
    }

    @Test
    fun `updates existing accounts`() {
        val account1 = BankAccount(BankAccountId.random(), Amount(10))
        repository.add(account1)
        val account2 = BankAccount(BankAccountId.random(), Amount(20))
        repository.add(account2)

        val updatedAccount1 = BankAccount(account1.id, Amount(50))
        repository.update(updatedAccount1)

        assertThat(repository.list(), equalTo(listOf(updatedAccount1, account2)))
    }
}