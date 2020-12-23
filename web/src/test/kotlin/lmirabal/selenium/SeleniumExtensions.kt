package lmirabal.selenium

import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement

fun SearchContext.getTableRows(): List<WebElement> {
    val accountsTable = getElement(By.tagName("tbody"))
    return accountsTable.getElements(By.cssSelector("tr"))
}

fun SearchContext.getElement(by: By): WebElement =
    findElement(by) ?: throw AssertionError("Could not find element $by")

fun SearchContext.getElements(by: By): List<WebElement> =
    findElements(by) ?: throw AssertionError("Could not find element $by")

fun WebElement.getTableColumn(index: Int): String {
    return getElement(By.cssSelector("td:nth-child(${index + 1})")).text
}