package com.example.ui.admin

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import com.example.ui.customer.CustomerImportMenuModal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AdminRepository
import com.example.model.Category
import com.example.model.Combo
import com.example.model.DiscountType
import com.example.model.FoodType
import com.example.model.Offer
import com.example.model.Product
import com.example.model.RestaurantSettings
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.FoodTypeBadge
import com.example.ui.components.PriceDisplay
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

enum class AdminTab(val label: String) {
    DASHBOARD("Dashboard"),
    PRODUCTS("Dishes"),
    CATEGORIES("Categories"),
    OFFERS_COMBOS("Offers"),
    SETTINGS("Settings")
}

@Composable
fun AdminMainScreen(
    repository: AdminRepository,
    onSwitchToCustomer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by repository.settings.collectAsState()
    var currentTab by remember { mutableStateOf(AdminTab.DASHBOARD) }

    // Dialogs
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    var editingOffer by remember { mutableStateOf<Offer?>(null) }
    var showAddOfferDialog by remember { mutableStateOf(false) }

    var editingCombo by remember { mutableStateOf<Combo?>(null) }
    var showAddComboDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = currentTab != AdminTab.DASHBOARD) {
        currentTab = AdminTab.DASHBOARD
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = RajputCharcoal,
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = RajputRed,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("R", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Rajput Admin",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Catalogue Manager (Offline)",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onSwitchToCustomer,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Customer App", fontSize = 11.sp)
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == AdminTab.DASHBOARD,
                    onClick = { currentTab = AdminTab.DASHBOARD },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AdminTab.PRODUCTS,
                    onClick = { currentTab = AdminTab.PRODUCTS },
                    icon = { Icon(Icons.Default.Fastfood, contentDescription = "Dishes") },
                    label = { Text("Dishes", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AdminTab.CATEGORIES,
                    onClick = { currentTab = AdminTab.CATEGORIES },
                    icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
                    label = { Text("Categories", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AdminTab.OFFERS_COMBOS,
                    onClick = { currentTab = AdminTab.OFFERS_COMBOS },
                    icon = { Icon(Icons.Default.LocalOffer, contentDescription = "Offers") },
                    label = { Text("Offers", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AdminTab.SETTINGS,
                    onClick = { currentTab = AdminTab.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AdminTab.DASHBOARD -> {
                    AdminDashboardTab(
                        repository = repository,
                        onNavigateToProducts = { currentTab = AdminTab.PRODUCTS },
                        onNavigateToCategories = { currentTab = AdminTab.CATEGORIES },
                        onNavigateToOffers = { currentTab = AdminTab.OFFERS_COMBOS },
                        onNavigateToSettings = { currentTab = AdminTab.SETTINGS }
                    )
                }

                AdminTab.PRODUCTS -> {
                    AdminProductsTab(
                        repository = repository,
                        onAddProduct = { showAddProductDialog = true },
                        onEditProduct = { editingProduct = it }
                    )
                }

                AdminTab.CATEGORIES -> {
                    AdminCategoriesTab(
                        repository = repository,
                        onAddCategory = { showAddCategoryDialog = true },
                        onEditCategory = { editingCategory = it }
                    )
                }

                AdminTab.OFFERS_COMBOS -> {
                    AdminOffersAndCombosTab(
                        repository = repository,
                        onAddOffer = { showAddOfferDialog = true },
                        onEditOffer = { editingOffer = it },
                        onAddCombo = { showAddComboDialog = true },
                        onEditCombo = { editingCombo = it }
                    )
                }

                AdminTab.SETTINGS -> {
                    AdminSettingsTab(
                        repository = repository,
                        onSwitchToCustomer = onSwitchToCustomer
                    )
                }
            }
        }
    }

    // Add / Edit Product Dialog
    if (showAddProductDialog || editingProduct != null) {
        AddEditProductModal(
            productToEdit = editingProduct,
            categories = repository.categories.collectAsState().value,
            onDismiss = {
                showAddProductDialog = false
                editingProduct = null
            },
            onSave = { product ->
                repository.saveProduct(product)
                showAddProductDialog = false
                editingProduct = null
            }
        )
    }

    // Add / Edit Category Dialog
    if (showAddCategoryDialog || editingCategory != null) {
        AddEditCategoryModal(
            categoryToEdit = editingCategory,
            onDismiss = {
                showAddCategoryDialog = false
                editingCategory = null
            },
            onSave = { cat ->
                repository.saveCategory(cat)
                showAddCategoryDialog = false
                editingCategory = null
            }
        )
    }

    // Add / Edit Offer Dialog
    if (showAddOfferDialog || editingOffer != null) {
        AddEditOfferModal(
            offerToEdit = editingOffer,
            onDismiss = {
                showAddOfferDialog = false
                editingOffer = null
            },
            onSave = { offer ->
                repository.saveOffer(offer)
                showAddOfferDialog = false
                editingOffer = null
            }
        )
    }

    // Add / Edit Combo Dialog
    if (showAddComboDialog || editingCombo != null) {
        AddEditComboModal(
            comboToEdit = editingCombo,
            availableProducts = repository.products.collectAsState().value,
            onDismiss = {
                showAddComboDialog = false
                editingCombo = null
            },
            onSave = { combo ->
                repository.saveCombo(combo)
                showAddComboDialog = false
                editingCombo = null
            }
        )
    }
}

/**
 * Admin Dashboard Tab
 */
@Composable
fun AdminDashboardTab(
    repository: AdminRepository,
    onNavigateToProducts: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val products by repository.products.collectAsState()
    val categories by repository.categories.collectAsState()
    val offers by repository.offers.collectAsState()
    val combos by repository.combos.collectAsState()
    val settings by repository.settings.collectAsState()

    val totalProducts = products.size
    val availableProducts = products.count { it.isAvailable }
    val unavailableProducts = products.count { !it.isAvailable }
    val bestsellersCount = products.count { it.isBestseller }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome and Local Mode Notice
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Catalogue Overview",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = RajputTextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Data shown here is stored locally on this device. Use Export Menu in Settings to export a catalogue file to distribute to customer devices.",
                        fontSize = 12.sp,
                        color = RajputTextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Summary Counters Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardStatCard(
                        title = "Total Dishes",
                        value = "$totalProducts",
                        subtitle = "$availableProducts live in menu",
                        color = RajputRed,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToProducts
                    )
                    DashboardStatCard(
                        title = "Categories",
                        value = "${categories.size}",
                        subtitle = "${categories.count { it.isEnabled }} enabled",
                        color = RajputCharcoal,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToCategories
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardStatCard(
                        title = "Out of Stock",
                        value = "$unavailableProducts",
                        subtitle = "Dishes marked unavailable",
                        color = if (unavailableProducts > 0) RajputDarkRed else RajputSuccess,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToProducts
                    )
                    DashboardStatCard(
                        title = "Deals & Combos",
                        value = "${offers.size + combos.size}",
                        subtitle = "${offers.count { it.isEnabled }} active offers",
                        color = RajputGold,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToOffers
                    )
                }
            }
        }

        // Quick Actions Row
        item {
            Text(
                text = "Quick Catalogue Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = RajputTextDark
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateToProducts,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Dish", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onNavigateToSettings,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export Menu", fontSize = 12.sp)
                }
            }
        }

        // Restaurant Business Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Business Configuration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RajputTextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("WhatsApp Order Line: ${settings.whatsapp}", fontSize = 12.sp, color = RajputTextDark)
                    Text("Phone Helpline: ${settings.phone}", fontSize = 12.sp, color = RajputTextDark)
                    Text("Delivery Charge: ₹${settings.deliveryChargePaise / 100} (Free over ₹${settings.freeDeliveryThresholdPaise / 100})", fontSize = 12.sp, color = RajputTextMuted)
                    Text("Minimum Order: ₹${settings.minOrderPaise / 100}", fontSize = 12.sp, color = RajputTextMuted)

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(onClick = onNavigateToSettings) {
                        Text("Edit Business Settings >", color = RajputRed, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RajputBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 12.sp, color = RajputTextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 10.sp, color = RajputTextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

/**
 * Admin Products Management Tab
 */
@Composable
fun AdminProductsTab(
    repository: AdminRepository,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit
) {
    val context = LocalContext.current
    val products by repository.products.collectAsState()
    val categories by repository.categories.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    val filteredProducts = remember(products, searchQuery, selectedCategoryId) {
        products.filter { p ->
            val matchCat = selectedCategoryId == null || p.categoryId == selectedCategoryId
            val matchQuery = searchQuery.isBlank() || p.name.contains(searchQuery, ignoreCase = true)
            matchCat && matchQuery
        }
    }

    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Delete Dish?") },
            text = { Text("Are you sure you want to delete '${productToDelete?.name}' from the catalogue?") },
            confirmButton = {
                Button(
                    onClick = {
                        productToDelete?.let { repository.deleteProduct(it.id) }
                        productToDelete = null
                        Toast.makeText(context, "Dish deleted", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct,
                containerColor = RajputRed,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Dish")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAFAFA))
        ) {
            // Search and Category Filter
            Surface(color = Color.White, shadowElevation = 2.dp) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search dishes...", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RajputRed) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCategoryId == null,
                                onClick = { selectedCategoryId = null },
                                label = { Text("All (${products.size})", fontSize = 11.sp) }
                            )
                        }
                        items(categories) { cat ->
                            FilterChip(
                                selected = selectedCategoryId == cat.id,
                                onClick = { selectedCategoryId = cat.id },
                                label = { Text(cat.name, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, RajputBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(modifier = Modifier.weight(1f)) {
                                    FoodTypeBadge(foodType = product.foodType)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = product.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = RajputTextDark
                                        )
                                        PriceDisplay(
                                            pricePaise = product.pricePaise,
                                            originalPricePaise = product.originalPricePaise,
                                            priceFontSize = 14
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { onEditProduct(product) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = RajputDarkRed, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { productToDelete = product }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RajputRed, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Availability Switch
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (product.isAvailable) "Available" else "Out of Stock",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (product.isAvailable) RajputSuccess else RajputRed
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Switch(
                                        checked = product.isAvailable,
                                        onCheckedChange = { repository.toggleProductAvailability(product.id) },
                                        colors = SwitchDefaults.colors(checkedThumbColor = RajputRed, checkedTrackColor = Color(0xFFFFEBEE))
                                    )
                                }

                                // Bestseller Switch / Button
                                Surface(
                                    color = if (product.isBestseller) Color(0xFFFEF3C7) else RajputLightGrey,
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, if (product.isBestseller) RajputGold else RajputBorder),
                                    modifier = Modifier.clickable { repository.toggleProductBestseller(product.id) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = if (product.isBestseller) RajputGold else RajputTextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (product.isBestseller) "Bestseller" else "Mark Bestseller",
                                            fontSize = 11.sp,
                                            color = if (product.isBestseller) Color(0xFF92400E) else RajputTextDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Add / Edit Product Modal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductModal(
    productToEdit: Product?,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var categoryId by remember { mutableStateOf(productToEdit?.categoryId ?: categories.firstOrNull()?.id ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }
    var priceRupees by remember { mutableStateOf(if (productToEdit != null) "${productToEdit.priceRupees}" else "99") }
    var originalPriceRupees by remember { mutableStateOf(if (productToEdit != null && productToEdit.originalPriceRupees > 0) "${productToEdit.originalPriceRupees}" else "") }
    var foodType by remember { mutableStateOf(productToEdit?.foodType ?: FoodType.VEG) }
    var prepTime by remember { mutableStateOf("${productToEdit?.prepTimeMinutes ?: 15}") }
    var isAvailable by remember { mutableStateOf(productToEdit?.isAvailable ?: true) }
    var isBestseller by remember { mutableStateOf(productToEdit?.isBestseller ?: false) }

    var categoryExpanded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (productToEdit == null) "Add New Dish" else "Edit Dish Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    val currentCatName = categories.firstOrNull { it.id == categoryId }?.name ?: "Select Category"
                    OutlinedTextField(
                        value = currentCatName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    categoryId = cat.id
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = priceRupees,
                        onValueChange = { priceRupees = it },
                        label = { Text("Price (₹) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = originalPriceRupees,
                        onValueChange = { originalPriceRupees = it },
                        label = { Text("Original ₹ (strike)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Food Type (Veg / Non-veg)
                Text("Food Type", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = foodType == FoodType.VEG,
                        onClick = { foodType = FoodType.VEG }
                    )
                    Text("Veg", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = foodType == FoodType.NON_VEG,
                        onClick = { foodType = FoodType.NON_VEG }
                    )
                    Text("Non-Veg", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = prepTime,
                    onValueChange = { prepTime = it },
                    label = { Text("Prep Time (minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Available in Menu", fontSize = 13.sp)
                    Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mark as Bestseller", fontSize = 13.sp)
                    Switch(checked = isBestseller, onCheckedChange = { isBestseller = it })
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val pricePaise = (priceRupees.toDoubleOrNull() ?: 99.0) * 100
                            val origPaise = (originalPriceRupees.toDoubleOrNull() ?: 0.0) * 100
                            val prep = prepTime.toIntOrNull() ?: 15

                            val product = Product(
                                id = productToEdit?.id ?: "prod_${System.currentTimeMillis()}",
                                name = name.trim().ifBlank { "Special Dish" },
                                categoryId = categoryId,
                                description = description.trim(),
                                pricePaise = pricePaise.toLong(),
                                originalPricePaise = origPaise.toLong(),
                                foodType = foodType,
                                isAvailable = isAvailable,
                                isBestseller = isBestseller,
                                prepTimeMinutes = prep,
                                customizationGroups = productToEdit?.customizationGroups ?: emptyList()
                            )
                            onSave(product)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Save Dish")
                    }
                }
            }
        }
    }
}

/**
 * Admin Categories Tab
 */
@Composable
fun AdminCategoriesTab(
    repository: AdminRepository,
    onAddCategory: () -> Unit,
    onEditCategory: (Category) -> Unit
) {
    val context = LocalContext.current
    val categories by repository.categories.collectAsState()
    val products by repository.products.collectAsState()

    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var deleteErrorMsg by remember { mutableStateOf<String?>(null) }

    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("Delete Category?") },
            text = { Text("Are you sure you want to delete '${categoryToDelete?.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        val result = repository.deleteCategory(categoryToDelete!!.id)
                        if (result.isSuccess) {
                            Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT).show()
                            categoryToDelete = null
                        } else {
                            deleteErrorMsg = result.exceptionOrNull()?.message
                            categoryToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) { Text("Cancel") }
            }
        )
    }

    if (deleteErrorMsg != null) {
        AlertDialog(
            onDismissRequest = { deleteErrorMsg = null },
            title = { Text("Cannot Delete") },
            text = { Text(deleteErrorMsg ?: "") },
            confirmButton = {
                Button(onClick = { deleteErrorMsg = null }) { Text("OK") }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCategory,
                containerColor = RajputRed,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAFAFA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { category ->
                val prodCount = products.count { it.categoryId == category.id }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, RajputBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = category.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = RajputTextDark
                            )
                            Text(
                                text = "$prodCount dishes in category • ${if (category.isEnabled) "Active" else "Disabled"}",
                                fontSize = 12.sp,
                                color = if (category.isEnabled) RajputSuccess else RajputRed
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = category.isEnabled,
                                onCheckedChange = { repository.toggleCategoryEnabled(category.id) }
                            )
                            IconButton(onClick = { onEditCategory(category) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = RajputDarkRed, modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { categoryToDelete = category }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RajputRed, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditCategoryModal(
    categoryToEdit: Category?,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    var description by remember { mutableStateOf(categoryToEdit?.description ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = if (categoryToEdit == null) "Add Category" else "Edit Category",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
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
                            if (name.isNotBlank()) {
                                val cat = Category(
                                    id = categoryToEdit?.id ?: "cat_${System.currentTimeMillis()}",
                                    name = name.trim(),
                                    description = description.trim(),
                                    isEnabled = categoryToEdit?.isEnabled ?: true,
                                    displayOrder = categoryToEdit?.displayOrder ?: 1
                                )
                                onSave(cat)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

/**
 * Admin Offers & Combos Tab
 */
@Composable
fun AdminOffersAndCombosTab(
    repository: AdminRepository,
    onAddOffer: () -> Unit,
    onEditOffer: (Offer) -> Unit,
    onAddCombo: () -> Unit,
    onEditCombo: (Combo) -> Unit
) {
    val offers by repository.offers.collectAsState()
    val combos by repository.combos.collectAsState()
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Offers, 1: Combos

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if (selectedSection == 0) onAddOffer() else onAddCombo() },
                containerColor = RajputRed,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAFAFA))
        ) {
            Surface(color = Color.White, shadowElevation = 2.dp) {
                TabRow(selectedTabIndex = selectedSection, containerColor = Color.White, contentColor = RajputRed) {
                    Tab(
                        selected = selectedSection == 0,
                        onClick = { selectedSection = 0 },
                        text = { Text("Coupons (${offers.size})") }
                    )
                    Tab(
                        selected = selectedSection == 1,
                        onClick = { selectedSection = 1 },
                        text = { Text("Combos (${combos.size})") }
                    )
                }
            }

            if (selectedSection == 0) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(offers) { offer ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, RajputBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${offer.code} • ${offer.title}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = offer.description,
                                        fontSize = 11.sp,
                                        color = RajputTextMuted
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(
                                        checked = offer.isEnabled,
                                        onCheckedChange = { repository.toggleOfferEnabled(offer.id) }
                                    )
                                    IconButton(onClick = { onEditOffer(offer) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(onClick = { repository.deleteOffer(offer.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RajputRed, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(combos) { combo ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, RajputBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = combo.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    PriceDisplay(pricePaise = combo.pricePaise, originalPricePaise = combo.originalPricePaise)
                                    Text(
                                        text = combo.description,
                                        fontSize = 11.sp,
                                        color = RajputTextMuted
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(
                                        checked = combo.isAvailable,
                                        onCheckedChange = { repository.toggleComboAvailable(combo.id) }
                                    )
                                    IconButton(onClick = { onEditCombo(combo) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(onClick = { repository.deleteCombo(combo.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RajputRed, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditOfferModal(
    offerToEdit: Offer?,
    onDismiss: () -> Unit,
    onSave: (Offer) -> Unit
) {
    var code by remember { mutableStateOf(offerToEdit?.code ?: "RAJPUT50") }
    var title by remember { mutableStateOf(offerToEdit?.title ?: "Special Discount") }
    var description by remember { mutableStateOf(offerToEdit?.description ?: "") }
    var discountType by remember { mutableStateOf(offerToEdit?.discountType ?: DiscountType.FLAT) }
    var discountValue by remember { mutableStateOf(if (offerToEdit != null) "${if (offerToEdit.discountType == DiscountType.FLAT) offerToEdit.discountValue / 100 else offerToEdit.discountValue}" else "50") }
    var minOrderRupees by remember { mutableStateOf(if (offerToEdit != null) "${offerToEdit.minOrderPaise / 100}" else "299") }
    var validUntil by remember { mutableStateOf(offerToEdit?.validUntil ?: "Ongoing") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (offerToEdit == null) "Add Offer" else "Edit Offer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text("Coupon Code *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Offer Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = discountType == DiscountType.FLAT,
                        onClick = { discountType = DiscountType.FLAT }
                    )
                    Text("Flat ₹ OFF", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    RadioButton(
                        selected = discountType == DiscountType.PERCENTAGE,
                        onClick = { discountType = DiscountType.PERCENTAGE }
                    )
                    Text("Percentage %", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = discountValue,
                    onValueChange = { discountValue = it },
                    label = { Text(if (discountType == DiscountType.FLAT) "Discount in ₹" else "Discount %") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = minOrderRupees,
                    onValueChange = { minOrderRupees = it },
                    label = { Text("Min Order Value (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
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
                            val rawVal = discountValue.toLongOrNull() ?: 50L
                            val finalVal = if (discountType == DiscountType.FLAT) rawVal * 100 else rawVal
                            val minPaise = (minOrderRupees.toLongOrNull() ?: 0L) * 100

                            val off = Offer(
                                id = offerToEdit?.id ?: "offer_${System.currentTimeMillis()}",
                                code = code.trim(),
                                title = title.trim(),
                                description = description.trim(),
                                discountType = discountType,
                                discountValue = finalVal,
                                minOrderPaise = minPaise,
                                validUntil = validUntil.trim(),
                                isEnabled = offerToEdit?.isEnabled ?: true
                            )
                            onSave(off)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Save Offer")
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditComboModal(
    comboToEdit: Combo?,
    availableProducts: List<Product>,
    onDismiss: () -> Unit,
    onSave: (Combo) -> Unit
) {
    var name by remember { mutableStateOf(comboToEdit?.name ?: "Royal Feast Combo") }
    var description by remember { mutableStateOf(comboToEdit?.description ?: "") }
    var priceRupees by remember { mutableStateOf(if (comboToEdit != null) "${comboToEdit.pricePaise / 100}" else "249") }
    var originalRupees by remember { mutableStateOf(if (comboToEdit != null) "${comboToEdit.originalPricePaise / 100}" else "320") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = if (comboToEdit == null) "Add Combo Meal" else "Edit Combo Meal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Combo Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceRupees,
                        onValueChange = { priceRupees = it },
                        label = { Text("Combo Price (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = originalRupees,
                        onValueChange = { originalRupees = it },
                        label = { Text("Regular Price (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Included Items Summary") },
                    placeholder = { Text("e.g. 1x Burger + 1x Fries + 1x Drink") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
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
                            val pricePaise = (priceRupees.toLongOrNull() ?: 249L) * 100
                            val origPaise = (originalRupees.toLongOrNull() ?: 0L) * 100
                            val cmb = Combo(
                                id = comboToEdit?.id ?: "combo_${System.currentTimeMillis()}",
                                name = name.trim(),
                                description = description.trim(),
                                pricePaise = pricePaise,
                                originalPricePaise = origPaise,
                                isAvailable = comboToEdit?.isAvailable ?: true
                            )
                            onSave(cmb)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                    ) {
                        Text("Save Combo")
                    }
                }
            }
        }
    }
}

/**
 * Admin Settings, Export Menu & Import Menu Tab
 */
@Composable
fun AdminSettingsTab(
    repository: AdminRepository,
    onSwitchToCustomer: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val settings by repository.settings.collectAsState()

    var name by remember(settings) { mutableStateOf(settings.restaurantName) }
    var phone by remember(settings) { mutableStateOf(settings.phone) }
    var whatsapp by remember(settings) { mutableStateOf(settings.whatsapp) }
    var address by remember(settings) { mutableStateOf(settings.address) }
    var openingHours by remember(settings) { mutableStateOf(settings.openingHours) }
    var deliveryChargeRupees by remember(settings) { mutableStateOf("${settings.deliveryChargePaise / 100}") }
    var freeDeliveryRupees by remember(settings) { mutableStateOf("${settings.freeDeliveryThresholdPaise / 100}") }
    var minOrderRupees by remember(settings) { mutableStateOf("${settings.minOrderPaise / 100}") }
    var adminPin by remember(settings) { mutableStateOf(settings.adminPin) }

    var showExportDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset to Bundled Menu?") },
            text = { Text("This will revert all catalogue dishes, categories, and offers back to the initial default bundled data.") },
            confirmButton = {
                Button(
                    onClick = {
                        repository.resetToDefault()
                        showResetDialog = false
                        Toast.makeText(context, "Catalogue reset to defaults", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showExportDialog) {
        val exportedJson = remember { repository.exportCatalogueJson() }
        Dialog(onDismissRequest = { showExportDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Export Catalogue JSON", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Transfer this JSON file or text to update customer devices manually.",
                        fontSize = 12.sp,
                        color = RajputTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = exportedJson,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        maxLines = 8
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(exportedJson))
                                Toast.makeText(context, "Copied JSON to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy JSON", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, exportedJson)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Rajput Catalogue JSON")
                                context.startActivity(shareIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share...", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(
                        onClick = { showExportDialog = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }

    if (showImportDialog) {
        CustomerImportMenuModal(
            onDismiss = { showImportDialog = false },
            onImport = { json ->
                val result = repository.importCatalogueJson(json)
                if (result.isSuccess) {
                    Toast.makeText(context, result.getOrNull(), Toast.LENGTH_LONG).show()
                    showImportDialog = false
                } else {
                    Toast.makeText(context, "Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Business Settings Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Restaurant & Delivery Settings", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Restaurant Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = whatsapp,
                            onValueChange = { whatsapp = it },
                            label = { Text("WhatsApp Line") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Helpline Phone") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Restaurant Address") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = openingHours,
                        onValueChange = { openingHours = it },
                        label = { Text("Opening Hours") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = deliveryChargeRupees,
                            onValueChange = { deliveryChargeRupees = it },
                            label = { Text("Delivery Fee (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = freeDeliveryRupees,
                            onValueChange = { freeDeliveryRupees = it },
                            label = { Text("Free Above (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = adminPin,
                        onValueChange = { adminPin = it },
                        label = { Text("Admin PIN (Convenience Lock)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val feePaise = (deliveryChargeRupees.toLongOrNull() ?: 40L) * 100
                            val freePaise = (freeDeliveryRupees.toLongOrNull() ?: 499L) * 100
                            val minPaise = (minOrderRupees.toLongOrNull() ?: 149L) * 100

                            val newSettings = settings.copy(
                                restaurantName = name.trim(),
                                phone = phone.trim(),
                                whatsapp = whatsapp.trim(),
                                address = address.trim(),
                                openingHours = openingHours.trim(),
                                deliveryChargePaise = feePaise,
                                freeDeliveryThresholdPaise = freePaise,
                                minOrderPaise = minPaise,
                                adminPin = adminPin.trim()
                            )
                            repository.updateSettings(newSettings)
                            Toast.makeText(context, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Business Settings")
                    }
                }
            }
        }

        // Catalogue Distribution (Export / Import)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Catalogue Distribution & Backup", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Because this app works 100% offline without remote servers, you can export your edited menu as JSON and import it onto customer devices.",
                        fontSize = 12.sp,
                        color = RajputTextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showExportDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RajputCharcoal),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export Menu", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { showImportDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Import Menu", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, RajputRed)
                    ) {
                        Text("Reset Menu to Bundled Defaults", color = RajputRed, fontSize = 12.sp)
                    }
                }
            }
        }

        // Switch to Customer Mode
        item {
            Button(
                onClick = onSwitchToCustomer,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Rajput Customer App")
            }
        }
    }
}
