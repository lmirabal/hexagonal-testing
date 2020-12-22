package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

abstract class BankTest {
    abstract val bank: Bank

    @Test
    fun listsAccounts() {
        val account1 = bank.createAccount()
        val account2 = bank.createAccount()

        val accounts = bank.listAccounts()

        assertThat(accounts, equalTo(listOf(account1, account2)))
    }
}