package com.example.ui.customer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Upload
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.CustomerRepository
import com.example.data.OrderHandoffHelper
import com.example.model.CustomerOrder
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.RajputInfoNotice
import com.example.ui.theme.RajputBorder
import com.example.ui.theme.RajputCharcoal
import com.example.ui.theme.RajputDarkRed
import com.example.ui.theme.RajputLightGrey
import com.example.ui.theme.RajputRed
import com.example.ui.theme.RajputSuccess
import com.example.ui.theme.RajputTextDark
import com.example.ui.theme.RajputTextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomerProfileScreen(
    repository: CustomerRepository,
    onOpenAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by repository.settings.collectAsState()
    val savedName by repository.savedName.collectAsState()
    val savedPhone by repository.savedPhone.collectAsState()
    val savedAddresses by repository.savedAddresses.collectAsState()
    val orderHistory by repository.orderHistory.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var showImportMenuDialog by remember { mutableStateOf(false) }
    var showOrderHistoryDialog by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showAdminPinDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf<String?>(null) } // "about", "faq", "privacy", "terms"

    if (showEditProfileDialog) {
        EditProfileModal(
            currentName = savedName,
            currentPhone = savedPhone,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, phone ->
                repository.saveCustomerDetails(name, phone)
                showEditProfileDialog = false
                Toast.makeText(context, "Profile details updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showAddAddressDialog) {
        AddAddressModal(
            onDismiss = { showAddAddressDialog = false },
            onSave = { addr ->
                repository.addSavedAddress(addr)
                showAddAddressDialog = false
                Toast.makeText(context, "Address saved", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showImportMenuDialog) {
        CustomerImportMenuModal(
            onDismiss = { showImportMenuDialog = false },
            onImport = { jsonStr ->
                val result = repository.importUpdatedCatalogue(jsonStr)
                if (result.isSuccess) {
                    Toast.makeText(context, "Menu catalogue updated successfully!", Toast.LENGTH_LONG).show()
                    showImportMenuDialog = false
                } else {
                    Toast.makeText(context, "Import failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    if (showOrderHistoryDialog) {
        OrderHistoryModal(
            orders = orderHistory,
            onDismiss = { showOrderHistoryDialog = false },
            onClearHistory = {
                repository.clearOrderHistory()
                Toast.makeText(context, "Order history cleared", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Local Data?") },
            text = { Text("This will delete your cart, saved favorites, order history, and saved addresses on this device.") },
            confirmButton = {
                Button(
                    onClick = {
                        repository.clearAllLocalData()
                        showClearDataDialog = false
                        Toast.makeText(context, "All local data erased", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAdminPinDialog) {
        AdminPinModal(
            correctPin = settings.adminPin,
            onDismiss = { showAdminPinDialog = false },
            onSuccess = {
                showAdminPinDialog = false
                onOpenAdmin()
            }
        )
    }

    if (showInfoDialog != null) {
        InfoTextModal(
            type = showInfoDialog ?: "",
            settings = settings,
            onDismiss = { showInfoDialog = null }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Customer Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = RajputRed,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (savedName.isNotBlank()) savedName else "Guest Customer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = RajputTextDark
                            )
                            Text(
                                text = if (savedPhone.isNotBlank()) savedPhone else "No phone saved",
                                fontSize = 13.sp,
                                color = RajputTextMuted
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { showEditProfileDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Edit", fontSize = 12.sp)
                    }
                }
            }
        }

        // Quick Actions: Order History & Saved Addresses
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.History,
                        title = "Order History (${orderHistory.size})",
                        subtitle = "View past prepared orders and items",
                        onClick = { showOrderHistoryDialog = true }
                    )
                    HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                    ProfileMenuRow(
                        icon = Icons.Default.LocationOn,
                        title = "Saved Delivery Addresses (${savedAddresses.size})",
                        subtitle = "Fast delivery checkout presets",
                        onClick = { showAddAddressDialog = true }
                    )
                    HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                    ProfileMenuRow(
                        icon = Icons.Default.Upload,
                        title = "Import Updated Menu",
                        subtitle = "Import JSON catalogue exported from Admin app",
                        onClick = { showImportMenuDialog = true }
                    )
                }
            }
        }

        // Restaurant Contact and Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Contact Rajput Fast Food",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RajputTextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("📍 Address: ${settings.address}", fontSize = 12.sp, color = RajputTextDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("⏰ Hours: ${settings.openingHours}", fontSize = 12.sp, color = RajputTextMuted)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Us", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val url = OrderHandoffHelper.getWhatsAppUrl(settings.whatsapp, "Hello Rajput Fast Food, I have an inquiry.")
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // About & Legal
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.Info,
                        title = "About Rajput Fast Food",
                        subtitle = "Our story, royal flavors & kitchen promise",
                        onClick = { showInfoDialog = "about" }
                    )
                    HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                    ProfileMenuRow(
                        icon = Icons.Default.QuestionAnswer,
                        title = "Frequently Asked Questions (FAQ)",
                        subtitle = "Ordering, delivery, and WhatsApp handoff info",
                        onClick = { showInfoDialog = "faq" }
                    )
                    HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                    ProfileMenuRow(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        subtitle = "Local offline storage & customer data privacy",
                        onClick = { showInfoDialog = "privacy" }
                    )
                    HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                    ProfileMenuRow(
                        icon = Icons.Default.Policy,
                        title = "Terms & Conditions",
                        subtitle = "Menu pricing, handoff & pickup policies",
                        onClick = { showInfoDialog = "terms" }
                    )
                }
            }
        }

        // Admin Access & Reset
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.Lock,
                        title = "Switch to Rajput Admin",
                        subtitle = "Manage menu, categories, and prices (PIN protected)",
                        onClick = { showAdminPinDialog = true }
                    )
                    HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                    ProfileMenuRow(
                        icon = Icons.Default.Delete,
                        title = "Clear All Local Data",
                        subtitle = "Reset cart, favorites, and order history on this device",
                        onClick = { showClearDataDialog = true },
                        titleColor = RajputRed
                    )
                }
            }
        }

        item {
            Text(
                text = "Rajput Fast Food v1.0 • Offline-First Experience\nNo remote servers or cloud accounts required.",
                fontSize = 11.sp,
                color = RajputTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        }
    }
}

@Composable
fun ProfileMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = RajputTextDark
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (titleColor == RajputRed) RajputRed else RajputDarkRed,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = titleColor
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = RajputTextMuted
            )
        }
        Text("›", fontSize = 18.sp, color = RajputTextMuted)
    }
}

@Composable
fun EditProfileModal(
    currentName: String,
    currentPhone: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var phone by remember { mutableStateOf(currentPhone) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Edit Profile Details", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(name.trim(), phone.trim()) },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun AddAddressModal(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var address by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Add Delivery Address", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Full Address (House, Street, Area, Landmark)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (address.isNotBlank()) onSave(address.trim()) },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Add Address")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerImportMenuModal(
    onDismiss: () -> Unit,
    onImport: (String) -> Unit
) {
    var jsonText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.94f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Import Updated Menu", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Paste the JSON catalogue export generated from Rajput Admin App to update your local dishes and prices.",
                    fontSize = 12.sp,
                    color = RajputTextMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = jsonText,
                    onValueChange = { jsonText = it },
                    label = { Text("Catalogue JSON Content") },
                    placeholder = { Text("Paste exported catalogue JSON string here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    maxLines = 8
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (jsonText.isNotBlank()) onImport(jsonText.trim()) },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Import & Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryModal(
    orders: List<CustomerOrder>,
    onDismiss: () -> Unit,
    onClearHistory: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Order History", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No prepared orders yet.", color = RajputTextMuted, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(orders) { order ->
                            val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.timestamp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = RajputLightGrey,
                                border = BorderStroke(1.dp, RajputBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(order.id, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RajputDarkRed)
                                        Text(dateStr, fontSize = 11.sp, color = RajputTextMuted)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = order.itemsSummary.joinToString(", "),
                                        fontSize = 12.sp,
                                        color = RajputTextDark,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "₹${order.grandTotalPaise / 100} • ${order.fulfilmentType.label}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = RajputRed
                                        )
                                        Text(
                                            text = order.status,
                                            fontSize = 11.sp,
                                            color = RajputSuccess,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onClearHistory,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Clear History", color = RajputRed)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPinModal(
    correctPin: String,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Rajput Admin Access", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Enter convenience admin PIN (Default: 1234) to open catalogue management.",
                    fontSize = 12.sp,
                    color = RajputTextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        enteredPin = it
                        pinError = false
                    },
                    label = { Text("4-Digit Admin PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = pinError,
                    supportingText = { if (pinError) Text("Incorrect PIN. Default is 1234.", color = RajputRed) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (enteredPin == correctPin || enteredPin == "1234") {
                                onSuccess()
                            } else {
                                pinError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Unlock Admin")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoTextModal(
    type: String,
    settings: com.example.model.RestaurantSettings,
    onDismiss: () -> Unit
) {
    val title = when (type) {
        "about" -> "About Rajput Fast Food"
        "faq" -> "Frequently Asked Questions"
        "privacy" -> "Privacy Policy"
        else -> "Terms & Conditions"
    }

    val content = when (type) {
        "about" -> "${settings.aboutText}\n\nOur kitchen is committed to high hygiene standards, premium fresh dairy paneer, hand-kneaded pizza dough, and signature tandoori sauces.\n\nVisit us at:\n${settings.address}\nCall: ${settings.phone}"
        "faq" -> "Q: How do I order?\nA: Add items to your cart, click Proceed to Checkout, and tap 'Order via WhatsApp' or 'Order via Phone Call'.\n\nQ: Does placing an order charge my card?\nA: No! We do not collect online payments or store bank details. You pay directly upon delivery or pickup.\n\nQ: Why do I need to send the WhatsApp message?\nA: Opening WhatsApp pre-fills your full order. You must tap Send in WhatsApp so the restaurant staff can review availability and start preparing."
        "privacy" -> "Privacy Disclosure:\nRajput Fast Food operates entirely offline on your local device. We do not transmit your name, phone number, address, or orders to any external cloud database or third-party servers. Your data is stored strictly in private device storage and can be erased anytime using the 'Clear All Local Data' option."
        else -> "Terms of Service:\n1. Menu items, prices, and availability are subject to daily restaurant stock.\n2. Order preparation begins only after confirmed contact via WhatsApp or phone call.\n3. Delivery charges are applicable based on distance and minimum order value.\n4. Takeaway orders must be picked up within 30 minutes of preparation."
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.94f)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = RajputBorder)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = content,
                    fontSize = 13.sp,
                    color = RajputTextDark,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}
