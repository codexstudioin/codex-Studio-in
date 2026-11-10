package com.example.ui.customer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TakeoutDining
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.CustomerRepository
import com.example.data.OrderHandoffHelper
import com.example.model.CartItem
import com.example.model.CustomerOrder
import com.example.model.Offer
import com.example.model.OrderFulfilmentType
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.FoodTypeBadge
import com.example.ui.components.QuantityStepper
import com.example.ui.components.RajputInfoNotice
import com.example.ui.theme.RajputBorder
import com.example.ui.theme.RajputCharcoal
import com.example.ui.theme.RajputDarkRed
import com.example.ui.theme.RajputGold
import com.example.ui.theme.RajputLightGrey
import com.example.ui.theme.RajputRed
import com.example.ui.theme.RajputSuccess
import com.example.ui.theme.RajputTextDark
import com.example.ui.theme.RajputTextMuted

@Composable
fun CustomerCartScreen(
    repository: CustomerRepository,
    fulfilmentType: OrderFulfilmentType,
    onFulfilmentChange: (OrderFulfilmentType) -> Unit,
    onExploreMenu: () -> Unit,
    onProceedToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cart by repository.cart.collectAsState()
    val settings by repository.settings.collectAsState()
    val offers by repository.offers.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }
    var enteredCoupon by remember { mutableStateOf("") }
    var appliedOffer by remember { mutableStateOf<Offer?>(null) }
    var couponError by remember { mutableStateOf<String?>(null) }

    // Calculation in paise
    val subtotalPaise = remember(cart) { cart.sumOf { it.totalPaise } }

    val deliveryFeePaise = remember(fulfilmentType, subtotalPaise, settings) {
        if (fulfilmentType == OrderFulfilmentType.TAKEAWAY) {
            0L
        } else if (subtotalPaise >= settings.freeDeliveryThresholdPaise) {
            0L
        } else {
            settings.deliveryChargePaise
        }
    }

    val discountPaise = remember(appliedOffer, subtotalPaise) {
        appliedOffer?.calculateDiscountPaise(subtotalPaise) ?: 0L
    }

    val grandTotalPaise = remember(subtotalPaise, deliveryFeePaise, discountPaise) {
        (subtotalPaise + deliveryFeePaise - discountPaise).coerceAtLeast(0L)
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Cart?") },
            text = { Text("Are you sure you want to remove all items from your cart?") },
            confirmButton = {
                Button(
                    onClick = {
                        repository.clearCart()
                        appliedOffer = null
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                ) {
                    Text("Clear Cart")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (cart.isEmpty()) {
        EmptyPlaceholder(
            title = "Your Cart is Empty",
            message = "Add delicious royal burgers, pizzas, momos, or combos to satisfy your cravings!",
            actionLabel = "Browse Rajput Menu",
            onActionClick = onExploreMenu,
            modifier = modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Clear Cart button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Shopping Cart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = RajputTextDark
                        )
                        Text(
                            text = "${cart.sumOf { it.quantity }} items in cart",
                            fontSize = 12.sp,
                            color = RajputTextMuted
                        )
                    }

                    TextButton(
                        onClick = { showClearDialog = true },
                        modifier = Modifier.testTag("clear_cart_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            tint = RajputRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Clear Cart", color = RajputRed, fontSize = 12.sp)
                    }
                }
            }

            // Fulfilment Switcher
            item {
                FulfilmentToggleBar(
                    selectedType = fulfilmentType,
                    deliveryEnabled = settings.deliveryEnabled,
                    takeawayEnabled = settings.takeawayEnabled,
                    onSelect = onFulfilmentChange
                )
            }

            // Items List
            items(cart, key = { it.cartItemId }) { item ->
                CartItemRow(
                    item = item,
                    onIncrease = { repository.updateCartQuantity(item.cartItemId, item.quantity + 1) },
                    onDecrease = { repository.updateCartQuantity(item.cartItemId, item.quantity - 1) },
                    onRemove = { repository.removeCartItem(item.cartItemId) }
                )
            }

            // Add More Items row
            item {
                OutlinedButton(
                    onClick = onExploreMenu,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, RajputRed)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = RajputRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add More Dishes", color = RajputRed, fontWeight = FontWeight.Bold)
                }
            }

            // Offers & Coupon Code Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, RajputBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = RajputRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Coupons & Discounts",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = RajputTextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (appliedOffer != null) {
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, RajputSuccess),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Coupon Applied: ${appliedOffer?.code}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF166534)
                                        )
                                        Text(
                                            text = "You saved ₹${discountPaise / 100} on this order!",
                                            fontSize = 11.sp,
                                            color = Color(0xFF166534)
                                        )
                                    }
                                    IconButton(
                                        onClick = { appliedOffer = null },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color(0xFF166534))
                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = enteredCoupon,
                                    onValueChange = {
                                        enteredCoupon = it.uppercase()
                                        couponError = null
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("coupon_input"),
                                    placeholder = { Text("Enter Coupon Code", fontSize = 12.sp) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RajputRed,
                                        unfocusedBorderColor = RajputBorder
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val match = offers.firstOrNull {
                                            it.isEnabled && it.code.equals(enteredCoupon.trim(), ignoreCase = true)
                                        }
                                        if (match == null) {
                                            couponError = "Invalid coupon code."
                                        } else if (subtotalPaise < match.minOrderPaise) {
                                            couponError = "Minimum order of ₹${match.minOrderPaise / 100} required for this coupon."
                                        } else {
                                            appliedOffer = match
                                            couponError = null
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                                    modifier = Modifier.testTag("apply_coupon_button")
                                ) {
                                    Text("Apply", fontWeight = FontWeight.Bold)
                                }
                            }

                            if (couponError != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = couponError ?: "",
                                    color = RajputRed,
                                    fontSize = 11.sp
                                )
                            }

                            // Quick available coupon tags
                            val availableCoupons = offers.filter { it.isEnabled }
                            if (availableCoupons.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Available for you:", fontSize = 11.sp, color = RajputTextMuted)
                                Spacer(modifier = Modifier.height(4.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(availableCoupons) { off ->
                                        Surface(
                                            color = RajputLightGrey,
                                            shape = RoundedCornerShape(6.dp),
                                            border = BorderStroke(1.dp, RajputBorder),
                                            modifier = Modifier.clickable {
                                                enteredCoupon = off.code
                                                if (subtotalPaise >= off.minOrderPaise) {
                                                    appliedOffer = off
                                                    couponError = null
                                                } else {
                                                    couponError = "Min order of ₹${off.minOrderPaise / 100} required."
                                                }
                                            }
                                        ) {
                                            Text(
                                                text = "${off.code} (Save up to ₹${off.discountValue / 100})",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = RajputRed,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bill Breakdown Details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, RajputBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Bill Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = RajputTextDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        BillRow(title = "Item Subtotal", amountRupees = subtotalPaise / 100)

                        if (fulfilmentType == OrderFulfilmentType.DELIVERY) {
                            val deliveryText = if (deliveryFeePaise == 0L) "FREE" else "₹${deliveryFeePaise / 100}"
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Fee", fontSize = 13.sp, color = RajputTextDark)
                                Text(
                                    deliveryText,
                                    fontSize = 13.sp,
                                    color = if (deliveryFeePaise == 0L) RajputSuccess else RajputTextDark,
                                    fontWeight = if (deliveryFeePaise == 0L) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        if (discountPaise > 0L) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Coupon Discount", fontSize = 13.sp, color = RajputSuccess)
                                Text("-₹${discountPaise / 100}", fontSize = 13.sp, color = RajputSuccess, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = RajputBorder)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Grand Total",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = RajputTextDark
                            )
                            Text(
                                text = "₹${grandTotalPaise / 100}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = RajputRed
                            )
                        }
                    }
                }
            }

            // Handoff Note
            item {
                RajputInfoNotice(
                    message = "Orders are prepared locally and sent directly to Rajput Fast Food via WhatsApp or phone call. No online card payments needed."
                )
            }

            // Checkout CTA
            item {
                val minOrderMet = subtotalPaise >= settings.minOrderPaise

                Column {
                    if (!minOrderMet) {
                        Text(
                            text = "Minimum order value is ₹${settings.minOrderPaise / 100}. Add ₹${(settings.minOrderPaise - subtotalPaise) / 100} more to checkout.",
                            color = RajputRed,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        )
                    }

                    Button(
                        onClick = onProceedToCheckout,
                        enabled = minOrderMet,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("proceed_to_checkout_button")
                    ) {
                        Text(
                            text = "Proceed to Checkout • ₹${grandTotalPaise / 100}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RajputBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    FoodTypeBadge(foodType = item.product.foodType, modifier = Modifier.padding(top = 2.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = item.product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = RajputTextDark
                        )
                        Text(
                            text = "₹${item.unitPricePaise / 100} each",
                            fontSize = 12.sp,
                            color = RajputTextMuted
                        )
                    }
                }

                QuantityStepper(
                    quantity = item.quantity,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease
                )
            }

            // Options summary
            if (item.selectedOptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item.selectedOptions.forEach { opt ->
                        Surface(
                            color = RajputLightGrey,
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(0.5.dp, RajputBorder)
                        ) {
                            Text(
                                text = opt.name,
                                fontSize = 10.sp,
                                color = RajputTextDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            if (item.specialInstructions.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Note: ${item.specialInstructions}",
                    fontSize = 11.sp,
                    color = RajputDarkRed,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onRemove, modifier = Modifier.height(26.dp)) {
                    Text("Remove", fontSize = 11.sp, color = RajputTextMuted)
                }

                Text(
                    text = "Total: ₹${item.totalPaise / 100}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = RajputTextDark
                )
            }
        }
    }
}

@Composable
fun BillRow(title: String, amountRupees: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 13.sp, color = RajputTextDark)
        Text("₹$amountRupees", fontSize = 13.sp, color = RajputTextDark, fontWeight = FontWeight.Medium)
    }
}

/**
 * Checkout Dialog / Sheet for entering customer name, phone, address,
 * and performing WhatsApp or Phone handoff.
 */
@Composable
fun CheckoutDialog(
    repository: CustomerRepository,
    fulfilmentType: OrderFulfilmentType,
    appliedCoupon: String,
    onDismiss: () -> Unit,
    onOrderHandedOff: (CustomerOrder) -> Unit
) {
    val context = LocalContext.current
    val cart by repository.cart.collectAsState()
    val settings by repository.settings.collectAsState()
    val savedName by repository.savedName.collectAsState()
    val savedPhone by repository.savedPhone.collectAsState()
    val savedAddresses by repository.savedAddresses.collectAsState()

    var name by remember { mutableStateOf(savedName) }
    var phone by remember { mutableStateOf(savedPhone) }
    var address by remember { mutableStateOf(savedAddresses.firstOrNull() ?: "") }
    var landmark by remember { mutableStateOf("") }
    var pickupTime by remember { mutableStateOf("Ready in 20 mins") }
    var specialInstructions by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    val subtotalPaise = remember(cart) { cart.sumOf { it.totalPaise } }
    val deliveryFeePaise = remember(fulfilmentType, subtotalPaise, settings) {
        if (fulfilmentType == OrderFulfilmentType.TAKEAWAY || subtotalPaise >= settings.freeDeliveryThresholdPaise) 0L
        else settings.deliveryChargePaise
    }
    // Calculate coupon discount
    val offers by repository.offers.collectAsState()
    val matchedOffer = offers.firstOrNull { it.code.equals(appliedCoupon, ignoreCase = true) }
    val discountPaise = matchedOffer?.calculateDiscountPaise(subtotalPaise) ?: 0L
    val grandTotalPaise = (subtotalPaise + deliveryFeePaise - discountPaise).coerceAtLeast(0L)

    fun validate(): Boolean {
        var valid = true
        if (name.trim().isBlank()) {
            nameError = true
            valid = false
        } else {
            nameError = false
        }

        val cleanPhone = phone.trim().replace(" ", "").replace("-", "")
        if (cleanPhone.length < 10) {
            phoneError = true
            valid = false
        } else {
            phoneError = false
        }

        if (fulfilmentType == OrderFulfilmentType.DELIVERY && address.trim().isBlank()) {
            addressError = true
            valid = false
        } else {
            addressError = false
        }
        return valid
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp))
                .testTag("checkout_modal"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Checkout Order",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = RajputTextDark
                        )
                        Text(
                            text = "Fulfilment: ${fulfilmentType.label}",
                            fontSize = 12.sp,
                            color = RajputRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Customer Name
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name_input"),
                    label = { Text("Your Full Name *", fontSize = 12.sp) },
                    placeholder = { Text("e.g. Vikram Singh", fontSize = 12.sp) },
                    isError = nameError,
                    supportingText = { if (nameError) Text("Name is required", color = RajputRed) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Contact Phone Number
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone_input"),
                    label = { Text("Mobile Number (10 digits) *", fontSize = 12.sp) },
                    placeholder = { Text("e.g. 9876543210", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneError,
                    supportingText = { if (phoneError) Text("Please enter a valid 10-digit number", color = RajputRed) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (fulfilmentType == OrderFulfilmentType.DELIVERY) {
                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            addressError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_address_input"),
                        label = { Text("Delivery Address (House/Flat, Street, Area) *", fontSize = 12.sp) },
                        placeholder = { Text("Enter your complete delivery address", fontSize = 12.sp) },
                        isError = addressError,
                        supportingText = { if (addressError) Text("Delivery address is required", color = RajputRed) },
                        maxLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = landmark,
                        onValueChange = { landmark = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nearby Landmark (optional)", fontSize = 12.sp) },
                        placeholder = { Text("e.g. Near Shiv Mandir, Opposite SBI Bank", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                } else {
                    OutlinedTextField(
                        value = pickupTime,
                        onValueChange = { pickupTime = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Preferred Pickup Time", fontSize = 12.sp) },
                        placeholder = { Text("e.g. Ready in 20 mins or 8:30 PM", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = specialInstructions,
                    onValueChange = { specialInstructions = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Delivery / Order Instructions (optional)", fontSize = 12.sp) },
                    placeholder = { Text("e.g. Call before arrival, extra ketchup sachets", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Order Summary Pill
                Surface(
                    color = RajputLightGrey,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, RajputBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Payable:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("₹${grandTotalPaise / 100}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = RajputRed)
                        }
                        Text(
                            text = "${cart.size} items • ${fulfilmentType.label}",
                            fontSize = 11.sp,
                            color = RajputTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                RajputInfoNotice(
                    message = "Please Note: You must press Send in WhatsApp or speak to the staff on the phone call to place this order."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action 1: WhatsApp Handoff
                Button(
                    onClick = {
                        if (validate()) {
                            // Save profile details locally
                            repository.saveCustomerDetails(name.trim(), phone.trim())
                            if (fulfilmentType == OrderFulfilmentType.DELIVERY && address.isNotBlank()) {
                                repository.addSavedAddress(address.trim())
                            }

                            val orderMsg = OrderHandoffHelper.buildWhatsAppMessage(
                                restaurantName = settings.restaurantName,
                                customerName = name,
                                customerPhone = phone,
                                fulfilmentType = fulfilmentType,
                                items = cart,
                                subtotalPaise = subtotalPaise,
                                deliveryFeePaise = deliveryFeePaise,
                                discountPaise = discountPaise,
                                discountCode = appliedCoupon,
                                grandTotalPaise = grandTotalPaise,
                                deliveryAddress = address,
                                landmark = landmark,
                                pickupTime = pickupTime,
                                specialInstructions = specialInstructions
                            )

                            // Save order record
                            val newOrder = CustomerOrder(
                                fulfilmentType = fulfilmentType,
                                customerName = name.trim(),
                                customerPhone = phone.trim(),
                                deliveryAddress = address.trim(),
                                landmark = landmark.trim(),
                                pickupTime = pickupTime.trim(),
                                specialInstructions = specialInstructions.trim(),
                                itemsSummary = cart.map { "${it.product.name} x${it.quantity}" },
                                subtotalPaise = subtotalPaise,
                                deliveryChargePaise = deliveryFeePaise,
                                discountPaise = discountPaise,
                                grandTotalPaise = grandTotalPaise,
                                appliedCoupon = appliedCoupon,
                                handoffMethod = "WHATSAPP",
                                status = "Sent to WhatsApp"
                            )
                            repository.saveOrderRecord(newOrder)
                            repository.clearCart()

                            // Launch WhatsApp
                            val url = OrderHandoffHelper.getWhatsAppUrl(settings.whatsapp, orderMsg)
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "WhatsApp not available, launching dialler", Toast.LENGTH_SHORT).show()
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings.phone}"))
                                context.startActivity(dialIntent)
                            }

                            onOrderHandedOff(newOrder)
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("whatsapp_handoff_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "WhatsApp",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Order via WhatsApp",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action 2: Phone Call Handoff
                OutlinedButton(
                    onClick = {
                        if (validate()) {
                            repository.saveCustomerDetails(name.trim(), phone.trim())
                            if (fulfilmentType == OrderFulfilmentType.DELIVERY && address.isNotBlank()) {
                                repository.addSavedAddress(address.trim())
                            }

                            val newOrder = CustomerOrder(
                                fulfilmentType = fulfilmentType,
                                customerName = name.trim(),
                                customerPhone = phone.trim(),
                                deliveryAddress = address.trim(),
                                landmark = landmark.trim(),
                                pickupTime = pickupTime.trim(),
                                specialInstructions = specialInstructions.trim(),
                                itemsSummary = cart.map { "${it.product.name} x${it.quantity}" },
                                subtotalPaise = subtotalPaise,
                                deliveryChargePaise = deliveryFeePaise,
                                discountPaise = discountPaise,
                                grandTotalPaise = grandTotalPaise,
                                appliedCoupon = appliedCoupon,
                                handoffMethod = "PHONE",
                                status = "Call Initiated"
                            )
                            repository.saveOrderRecord(newOrder)
                            repository.clearCart()

                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings.phone}"))
                            context.startActivity(dialIntent)

                            onOrderHandedOff(newOrder)
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.5.dp, RajputRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("phone_handoff_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = RajputRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Order via Phone Call (${settings.phone})",
                        color = RajputRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Order Confirmation & Handoff Success Notice Dialog
 */
@Composable
fun OrderHandedOffDialog(
    order: CustomerOrder,
    onDismiss: () -> Unit,
    onViewHistory: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = RajputSuccess,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Order Prepared!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = RajputTextDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Order ID: ${order.id}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = RajputDarkRed
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (order.handoffMethod == "WHATSAPP") {
                        "Your order summary has been formatted and handed off to WhatsApp. Please tap Send in WhatsApp to complete your order!"
                    } else {
                        "Phone dialler opened. Please confirm items with the restaurant staff on the phone."
                    },
                    fontSize = 13.sp,
                    color = RajputTextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = RajputLightGrey,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Items: ${order.itemsSummary.joinToString(", ")}", fontSize = 11.sp, color = RajputTextDark)
                        Text("Total: ₹${order.grandTotalPaise / 100} • ${order.fulfilmentType.label}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RajputRed)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onViewHistory()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Order History", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                TextButton(onClick = onDismiss) {
                    Text("Back to Home")
                }
            }
        }
    }
}
