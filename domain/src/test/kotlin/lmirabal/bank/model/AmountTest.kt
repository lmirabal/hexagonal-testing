package lmirabal.bank.model

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.greaterThanOrEqualTo
import com.natpryce.hamkrest.lessThan
import com.natpryce.hamkrest.lessThanOrEqualTo
import com.natpryce.hamkrest.throws
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class AmountTest {
    @Test
    fun `value must be positive`() {
        assertThat({ Amount(-10) }, throws<IllegalArgumentException>())
    }

    @Test
    fun adds() {
        val result = Amount(10) + Amount(30)

        assertThat(result, equalTo(Amount(40)))
    }

    @Test
    fun subtracts() {
        val result = Amount(30) - Amount(10)

        assertThat(result, equalTo(Amount(20)))
    }

    @Test
    fun `can be compared`() {
        assertAll(
            { assertThat(Amount(10), lessThan(Amount(20))) },
            { assertThat(Amount(20), lessThanOrEqualTo(Amount(20))) },
            { assertThat(Amount(20), greaterThan(Amount(10))) },
            { assertThat(Amount(20), greaterThanOrEqualTo(Amount(20))) },
        )
    }
}