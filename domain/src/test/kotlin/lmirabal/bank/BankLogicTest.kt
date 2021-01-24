package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lmirabal.bank.data.InMemoryBankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.junit.jupiter.api.Test

class BankLogicTest : BankTest() {
    private val idFactory = RecordingIdFactory()
    override val bank = BankLogic(InMemoryBankAccountRepository(), idFactory)

    @Test
    fun createsAnAccount() {
        val account = bank.createAccount()

        val id = idFactory.last()
        assertThat(account, equalTo(BankAccount(id, Amount.ZERO)))
    }

    class RecordingIdFactory : () -> BankAccountId {
        private val values = mutableListOf<BankAccountId>()

        override fun invoke(): BankAccountId {
            return BankAccountId.random().also { values.add(it) }
        }

        fun last(): BankAccountId {
            return values.last()
        }
    }
}