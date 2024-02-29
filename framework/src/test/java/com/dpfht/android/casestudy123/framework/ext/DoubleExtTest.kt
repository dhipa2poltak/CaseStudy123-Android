package com.dpfht.android.casestudy123.framework.ext

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DoubleExtTest {

  @Test
  fun `ensure the correct String is returned when calling toRupiahString extension method`() {
    val expected = "Rp. 32.000"
    val balance = 32000.0
    val actual = balance.toRupiahString()

    assertTrue(expected == actual)
  }
}
