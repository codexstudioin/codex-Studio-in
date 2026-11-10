package com.example.ui.customer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TakeoutDining
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
import com.example.R
import com.example.data.CustomerRepository
import com.example.data.OrderHandoffHelper
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.Combo
import com.example.model.CustomerOrder
import com.example.model.CustomizationOption
import com.example.model.FoodType
import com.example.model.Offer
import com.example.model.OrderFulfilmentType
import com.example.model.Product
import com.example.model.RestaurantSettings
import com.example.ui.components.BestsellerTag
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.FoodCard
import com.example.ui.components.FoodTypeBadge
import com.example.ui.components.PriceDisplay
import com.example.ui.components.QuantityStepper
import com.example.ui.components.RajputInfoNotice
import com.example.ui.theme.RajputAccent
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
fun CustomerHomeScreen(
    repository: CustomerRepository,
    fulfilmentType: OrderFulfilmentType,
    onFulfilmentChange: (OrderFulfilmentType) -> Unit,
    onNavigateToMenu: (categoryId: String?) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onProductClick: (Product) -> Unit,
    onOpenAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by repository.settings.collectAsState()
    val categories by repository.categories.collectAsState()
    val products by repository.products.collectAsState()
    val combos by repository.combos.collectAsState()
    val offers by repository.offers.collectAsState()
    val favorites by repository.favorites.collectAsState()
    val cart by repository.cart.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val bestsellers = remember(products) {
        products.filter { it.isBestseller && it.isAvailable }
    }

    val activeCombos = remember(combos) {
        combos.filter { it.isAvailable }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item {
            CustomerTopHeader(
                restaurantName = settings.restaurantName,
                tagline = settings.tagline,
                address = settings.address,
                onOpenAdmin = onOpenAdmin
            )
        }

        // Fulfilment Switcher: Delivery vs Takeaway
        item {
            FulfilmentToggleBar(
                selectedType = fulfilmentType,
                deliveryEnabled = settings.deliveryEnabled,
                takeawayEnabled = settings.takeawayEnabled,
                onSelect = onFulfilmentChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Search Bar (leads to menu search)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.isNotBlank()) onNavigateToMenu(null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    placeholder = { Text("Search delicious burgers, pizza, momos...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = RajputRed
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RajputRed,
                        unfocusedBorderColor = RajputBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }

        // Hero Promotional Banner
        item {
            HeroBannerCard(
                onExploreMenu = { onNavigateToMenu(null) },
                onViewOffers = onNavigateToOffers,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Restaurant Admin Quick Entry Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable(onClick = onOpenAdmin)
                    .testTag("admin_portal_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = RajputCharcoal),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(RajputRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Restaurant Admin App", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            Text("Manage menu, dishes, prices & offers (PIN: 1234)", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                    Surface(
                        color = RajputRed,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Open >",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Food Categories Horizontal Scroll
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explore Categories",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = RajputTextDark
                    )
                    TextButton(onClick = { onNavigateToMenu(null) }) {
                        Text(
                            text = "View All",
                            color = RajputRed,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories.filter { it.isEnabled }) { category ->
                        CategoryTile(
                            category = category,
                            onClick = { onNavigateToMenu(category.id) }
                        )
                    }
                }
            }
        }

        // Bestsellers Section
        if (bestsellers.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 Royal Bestsellers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = RajputTextDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Most Loved",
                        fontSize = 12.sp,
                        color = RajputGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            items(bestsellers.take(4)) { product ->
                val cartQty = cart.filter { it.product.id == product.id }.sumOf { it.quantity }
                val isFav = favorites.contains(product.id)

                FoodCard(
                    product = product,
                    isFavorite = isFav,
                    cartQuantity = cartQty,
                    onItemClick = { onProductClick(product) },
                    onAddToCart = {
                        if (product.customizationGroups.isNotEmpty()) {
                            onProductClick(product)
                        } else {
                            repository.addToCart(product, 1)
                        }
                    },
                    onIncreaseQuantity = {
                        val item = cart.firstOrNull { it.product.id == product.id }
                        if (item != null) repository.updateCartQuantity(item.cartItemId, item.quantity + 1)
                    },
                    onDecreaseQuantity = {
                        val item = cart.firstOrNull { it.product.id == product.id }
                        if (item != null) repository.updateCartQuantity(item.cartItemId, item.quantity - 1)
                    },
                    onToggleFavorite = { repository.toggleFavorite(product.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Special Combos Section
        if (activeCombos.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👑 Royal Value Combos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = RajputTextDark
                    )
                    TextButton(onClick = onNavigateToOffers) {
                        Text("All Deals", color = RajputRed, fontSize = 13.sp)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(activeCombos) { combo ->
                        ComboCard(
                            combo = combo,
                            onAddToCart = { repository.addComboToCart(combo) }
                        )
                    }
                }
            }
        }

        // Active Offers Banner
        if (offers.any { it.isEnabled }) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ActiveOffersHighlight(
                    offers = offers.filter { it.isEnabled },
                    onViewOffers = onNavigateToOffers,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Restaurant Hours & Contact Info Card
        item {
            Spacer(modifier = Modifier.height(16.dp))
            RestaurantInfoCard(
                settings = settings,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun CustomerTopHeader(
    restaurantName: String,
    tagline: String,
    address: String,
    onOpenAdmin: () -> Unit
) {
    Surface(
        color = RajputRed,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rajput_logo),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = restaurantName,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 19.sp,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = tagline,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }

                // Admin Switch button
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .clickable(onClick = onOpenAdmin)
                        .testTag("switch_to_admin_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Admin App",
                            tint = RajputRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Admin App",
                            color = RajputRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address subtitle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = address,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun FulfilmentToggleBar(
    selectedType: OrderFulfilmentType,
    deliveryEnabled: Boolean,
    takeawayEnabled: Boolean,
    onSelect: (OrderFulfilmentType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = RajputLightGrey,
        border = BorderStroke(1.dp, RajputBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            // Delivery Option
            val isDeliverySelected = selectedType == OrderFulfilmentType.DELIVERY
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = deliveryEnabled) { onSelect(OrderFulfilmentType.DELIVERY) }
                    .testTag("toggle_delivery"),
                shape = RoundedCornerShape(10.dp),
                color = if (isDeliverySelected) RajputRed else Color.Transparent,
                shadowElevation = if (isDeliverySelected) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DeliveryDining,
                        contentDescription = null,
                        tint = if (isDeliverySelected) Color.White else RajputTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Doorstep Delivery",
                        fontWeight = if (isDeliverySelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (isDeliverySelected) Color.White else RajputTextDark
                    )
                }
            }

            // Takeaway Option
            val isTakeawaySelected = selectedType == OrderFulfilmentType.TAKEAWAY
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = takeawayEnabled) { onSelect(OrderFulfilmentType.TAKEAWAY) }
                    .testTag("toggle_takeaway"),
                shape = RoundedCornerShape(10.dp),
                color = if (isTakeawaySelected) RajputRed else Color.Transparent,
                shadowElevation = if (isTakeawaySelected) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TakeoutDining,
                        contentDescription = null,
                        tint = if (isTakeawaySelected) Color.White else RajputTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Self Pickup",
                        fontWeight = if (isTakeawaySelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (isTakeawaySelected) Color.White else RajputTextDark
                    )
                }
            }
        }
    }
}

@Composable
fun HeroBannerCard(
    onExploreMenu: () -> Unit,
    onViewOffers: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_banner_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = RajputCharcoal),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) {
            // Background food artwork
            Image(
                painter = painterResource(id = R.drawable.rajput_banner_1794317421025),
                contentDescription = "Rajput Fast Food Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay for text contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.88f),
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Overlay Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = RajputRed,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "FLAT ₹50 OFF",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Sizzling Royal Burgers\n& Cheesy Pizzas",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Button(
                        onClick = onExploreMenu,
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Order Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onViewOffers,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("View Offers", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryTile(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
            .testTag("category_tile_${category.id}")
    ) {
        Surface(
            shape = CircleShape,
            color = RajputLightGrey,
            border = BorderStroke(1.dp, RajputBorder),
            modifier = Modifier.size(62.dp),
            shadowElevation = 1.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFFEBEE), Color.White)
                        )
                    )
            ) {
                // Category emblem
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = category.name,
                    tint = RajputRed,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = RajputTextDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ComboCard(
    combo: Combo,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .testTag("combo_card_${combo.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(RajputDarkRed, RajputCharcoal)
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rajput_logo),
                    contentDescription = combo.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    color = RajputGold,
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "ROYAL COMBO",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = combo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = RajputTextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = combo.description,
                fontSize = 11.sp,
                color = RajputTextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriceDisplay(
                    pricePaise = combo.pricePaise,
                    originalPricePaise = combo.originalPricePaise,
                    priceFontSize = 15
                )

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("ADD +", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ActiveOffersHighlight(
    offers: List<Offer>,
    onViewOffers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val firstOffer = offers.firstOrNull() ?: return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onViewOffers),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        border = BorderStroke(1.dp, Color(0xFFFECACA))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(RajputRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = firstOffer.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = RajputDarkRed
                )
                Text(
                    text = "Use Code ${firstOffer.code} • ${firstOffer.description}",
                    fontSize = 11.sp,
                    color = RajputTextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "View >",
                color = RajputRed,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun RestaurantInfoCard(
    settings: RestaurantSettings,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RajputBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = RajputRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Restaurant Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = RajputTextDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📍 ${settings.address}",
                fontSize = 12.sp,
                color = RajputTextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "⏰ Opening Hours: ${settings.openingHours}",
                fontSize = 12.sp,
                color = RajputTextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🛵 Delivery Charge: ₹${settings.deliveryChargePaise / 100} (Free over ₹${settings.freeDeliveryThresholdPaise / 100})",
                fontSize = 12.sp,
                color = RajputTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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
                        val url = OrderHandoffHelper.getWhatsAppUrl(settings.whatsapp, "Hello Rajput Fast Food, I have an inquiry about the menu.")
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
