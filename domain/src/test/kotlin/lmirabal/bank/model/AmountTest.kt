package lmirabal.bank.model

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.jupiter.api.Test

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
}