package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DefaultMediaData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("DOOMS", appName)
  }

  @Test
  fun `test default media items seed generation`() {
    val items = DefaultMediaData.generateInitialItems()
    assertTrue(items.isNotEmpty())
    val mcuItems = items.filter { it.category == "mcu" }
    assertEquals(65, mcuItems.size)
    val xmenItems = items.filter { it.category == "xmen" }
    assertEquals(14, xmenItems.size)
    val seriesItems = items.filter { it.category == "series" }
    assertEquals(26, seriesItems.size)
  }
}

