package lmirabal.bank.model

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class BankAccountTest {
    @Test
    fun `increases balance on deposit`() {
        val id = BankAccountId.random()
        val account = BankAccount(id, Amount(100))

        val updatedAccount = account.deposit(Amount(200))

        assertThat(updatedAccount, equalTo(BankAccount(id, Amount(300))))
    }

    @Test
    fun `decreases balance on withdrawal`() {
        val id = BankAccountId.random()
        val account = BankAccount(id, Amount(300))

        val updatedAccount = account.withdraw(Amount(200))

        assertThat(updatedAccount, equalTo(BankAccount(id, Amount(100))))
    }
}