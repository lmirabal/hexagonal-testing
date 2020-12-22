package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lmirabal.bank.data.InMemoryBankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.junit.jupiter.api.Test

class BankServiceTest {
    private val id = BankAccountId.random()
    private val idFactory = { id }
    private val bank = BankService(InMemoryBankAccountRepository(), idFactory)

    @Test
    fun createsAnAccount() {
        val account = bank.createAccount()

        assertThat(account, equalTo(BankAccount(id, Amount.ZERO)))
    }

    @Test
    fun listsAccounts() {
        val account1 = bank.createAccount()
        val account2 = bank.createAccount()

        val accounts = bank.listAccounts()

        assertThat(accounts, equalTo(listOf(account1, account2)))
    }
}