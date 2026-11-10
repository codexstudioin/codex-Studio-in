package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DefaultData
import com.example.data.OrderHandoffHelper
import com.example.model.CartItem
import com.example.model.OrderFulfilmentType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        val adminAppName = context.getString(R.string.admin_app_name)
        assertEquals("Rajput Fast Food", appName)
        assertEquals("Rajput Admin", adminAppName)
    }

    @Test
    fun `verify default catalogue contains products and categories`() {
        assertTrue(DefaultData.defaultCategories.isNotEmpty())
        assertTrue(DefaultData.defaultProducts.isNotEmpty())
        assertTrue(DefaultData.defaultOffers.isNotEmpty())
        assertTrue(DefaultData.defaultCombos.isNotEmpty())
    }

    @Test
    fun `verify WhatsApp order message handoff format`() {
        val testProduct = DefaultData.defaultProducts.first()
        val cartItem = CartItem(product = testProduct, quantity = 2)

        val message = OrderHandoffHelper.buildWhatsAppMessage(
            restaurantName = "Rajput Fast Food",
            customerName = "Vikram Singh",
            customerPhone = "9876543210",
            fulfilmentType = OrderFulfilmentType.DELIVERY,
            items = listOf(cartItem),
            subtotalPaise = 29800L,
            deliveryFeePaise = 4000L,
            discountPaise = 5000L,
            discountCode = "RAJPUT50",
            grandTotalPaise = 28800L,
            deliveryAddress = "12 Royal Heritage, Station Road, Jaipur",
            landmark = "Near City Palace",
            pickupTime = "",
            specialInstructions = "Extra spicy"
        )

        assertTrue(message.contains("Hello Rajput Fast Food!"))
        assertTrue(message.contains("Customer: Vikram Singh"))
        assertTrue(message.contains("Order Type: Delivery"))
        assertTrue(message.contains("Grand Total: ₹288"))
        assertTrue(message.contains("Delivery Address:"))
        assertTrue(message.contains("12 Royal Heritage"))
    }

    @Test
    fun `launch MainActivity successfully`() {
        val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
        assertNotNull(controller.get())
    }

    @Test
    fun `launch AdminActivity successfully`() {
        val controller = org.robolectric.Robolectric.buildActivity(AdminActivity::class.java).setup()
        assertNotNull(controller.get())
    }
}
