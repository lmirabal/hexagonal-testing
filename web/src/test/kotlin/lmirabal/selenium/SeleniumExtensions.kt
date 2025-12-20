package lmirabal.selenium

import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement

fun SearchContext.getTableRows(): List<WebElement> {
    val accountsTable = findElement(By.tagName("tbody"))
    return accountsTable.findElements(By.cssSelector("tr"))
}

fun WebElement.getTableColumn(index: Int): String {
    return findElement(By.cssSelector("td:nth-child(${index + 1})")).text
}