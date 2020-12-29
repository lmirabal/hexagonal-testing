package lmirabal.bank.web

import lmirabal.bank.Bank
import lmirabal.bank.BankTest
import lmirabal.bank.http.BankHttpClient
import lmirabal.bank.http.bankHttp
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import lmirabal.selenium.getElement
import lmirabal.selenium.getTableColumn
import lmirabal.selenium.getTableRows
import org.http4k.core.HttpHandler
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import java.math.BigDecimal
import java.util.UUID

class BankWebTest : BankTest() {
    private val http: HttpHandler = bankHttp()
    private val httpClient: Bank = BankHttpClient(http)
    private val web: HttpHandler = bankWeb(httpClient)
    override val bank: Bank = BankWebDriver(web)
}

class BankWebDriver(web: HttpHandler) : Bank {
    private val driver = Http4kWebDriver(web)

    override fun createAccount(): BankAccount {
        driver.navigate().to("/")
        driver.getElement(By.id("create-account")).submit()

        return driver.getBankAccounts().last()
    }

    override fun listAccounts(): List<BankAccount> {
        return driver.getBankAccounts()
    }

    private fun SearchContext.getBankAccounts(): List<BankAccount> {
        return getTableRows().map { row -> row.toBankAccount() }
    }

    private fun WebElement.toBankAccount(): BankAccount {
        fun WebElement.getBalance(): Amount {
            val balanceTextInMajorUnits = getTableColumn(BALANCE_INDEX)
            val balanceInMinorUnits = BigDecimal(balanceTextInMajorUnits).movePointRight(2).toLong()
            return Amount(balanceInMinorUnits)
        }

        return BankAccount(getBankAccountId(), getBalance())
    }

    override fun deposit(id: BankAccountId, amount: Amount): BankAccount {
        val row = driver.getTableRows().first { row -> row.getBankAccountId() == id }

        row.getElement(By.id("amount")).sendKeys(amount.format())
        row.getElement(By.id("deposit")).submit()
        return driver.getBankAccounts().first { account -> account.id == id }
    }

    override fun withdraw(id: BankAccountId, amount: Amount): BankAccount {
        TODO("Not yet implemented")
    }

    private fun WebElement.getBankAccountId(): BankAccountId {
        val idText = getTableColumn(ID_INDEX)
        return BankAccountId(UUID.fromString(idText))
    }

    companion object {
        private const val ID_INDEX = 0
        private const val BALANCE_INDEX = 1
    }
}
