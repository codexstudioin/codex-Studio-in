package com.example.ui.customer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CustomerRepository
import com.example.model.CustomerOrder
import com.example.model.OrderFulfilmentType
import com.example.model.Product
import com.example.ui.theme.RajputBorder
import com.example.ui.theme.RajputDarkRed
import com.example.ui.theme.RajputRed
import com.example.ui.theme.RajputTextDark
import com.example.ui.theme.RajputTextMuted

enum class CustomerTab(val label: String) {
    HOME("Home"),
    MENU("Menu"),
    OFFERS("Offers"),
    FAVORITES("Favorites"),
    CART("Cart"),
    PROFILE("Profile")
}

@Composable
fun CustomerMainApp(
    repository: CustomerRepository,
    onOpenAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(CustomerTab.HOME) }
    var initialMenuCategoryId by remember { mutableStateOf<String?>(null) }
    var fulfilmentType by remember { mutableStateOf(OrderFulfilmentType.DELIVERY) }

    // Modals
    var detailedProduct by remember { mutableStateOf<Product?>(null) }
    var showCheckoutModal by remember { mutableStateOf(false) }
    var lastHandedOffOrder by remember { mutableStateOf<CustomerOrder?>(null) }
    var appliedCouponFromOffers by remember { mutableStateOf("") }

    val cart by repository.cart.collectAsState()
    val totalCartCount = remember(cart) { cart.sumOf { it.quantity } }

    BackHandler(enabled = currentTab != CustomerTab.HOME) {
        currentTab = CustomerTab.HOME
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                // Home
                NavigationBarItem(
                    selected = currentTab == CustomerTab.HOME,
                    onClick = { currentTab = CustomerTab.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE),
                        unselectedIconColor = RajputTextMuted,
                        unselectedTextColor = RajputTextMuted
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                // Menu
                NavigationBarItem(
                    selected = currentTab == CustomerTab.MENU,
                    onClick = {
                        initialMenuCategoryId = null
                        currentTab = CustomerTab.MENU
                    },
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = "Menu") },
                    label = { Text("Menu", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE),
                        unselectedIconColor = RajputTextMuted,
                        unselectedTextColor = RajputTextMuted
                    ),
                    modifier = Modifier.testTag("nav_menu")
                )

                // Offers
                NavigationBarItem(
                    selected = currentTab == CustomerTab.OFFERS,
                    onClick = { currentTab = CustomerTab.OFFERS },
                    icon = { Icon(Icons.Default.LocalOffer, contentDescription = "Offers") },
                    label = { Text("Offers", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE),
                        unselectedIconColor = RajputTextMuted,
                        unselectedTextColor = RajputTextMuted
                    ),
                    modifier = Modifier.testTag("nav_offers")
                )

                // Favorites
                NavigationBarItem(
                    selected = currentTab == CustomerTab.FAVORITES,
                    onClick = { currentTab = CustomerTab.FAVORITES },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE),
                        unselectedIconColor = RajputTextMuted,
                        unselectedTextColor = RajputTextMuted
                    ),
                    modifier = Modifier.testTag("nav_favorites")
                )

                // Cart with Count Badge
                NavigationBarItem(
                    selected = currentTab == CustomerTab.CART,
                    onClick = { currentTab = CustomerTab.CART },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (totalCartCount > 0) {
                                    Badge(containerColor = RajputRed, contentColor = Color.White) {
                                        Text("$totalCartCount")
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE),
                        unselectedIconColor = RajputTextMuted,
                        unselectedTextColor = RajputTextMuted
                    ),
                    modifier = Modifier.testTag("nav_cart")
                )

                // Profile
                NavigationBarItem(
                    selected = currentTab == CustomerTab.PROFILE,
                    onClick = { currentTab = CustomerTab.PROFILE },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RajputRed,
                        selectedTextColor = RajputRed,
                        indicatorColor = Color(0xFFFFEBEE),
                        unselectedIconColor = RajputTextMuted,
                        unselectedTextColor = RajputTextMuted
                    ),
                    modifier = Modifier.testTag("nav_profile")
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
                CustomerTab.HOME -> {
                    CustomerHomeScreen(
                        repository = repository,
                        fulfilmentType = fulfilmentType,
                        onFulfilmentChange = { fulfilmentType = it },
                        onNavigateToMenu = { catId ->
                            initialMenuCategoryId = catId
                            currentTab = CustomerTab.MENU
                        },
                        onNavigateToCart = { currentTab = CustomerTab.CART },
                        onNavigateToOffers = { currentTab = CustomerTab.OFFERS },
                        onProductClick = { detailedProduct = it },
                        onOpenAdmin = onOpenAdmin
                    )
                }

                CustomerTab.MENU -> {
                    CustomerMenuScreen(
                        repository = repository,
                        initialCategoryId = initialMenuCategoryId,
                        onProductClick = { detailedProduct = it }
                    )
                }

                CustomerTab.OFFERS -> {
                    CustomerOffersScreen(
                        repository = repository,
                        onApplyOfferToCart = { coupon ->
                            appliedCouponFromOffers = coupon
                            currentTab = CustomerTab.CART
                        },
                        onExploreMenu = { currentTab = CustomerTab.MENU }
                    )
                }

                CustomerTab.FAVORITES -> {
                    CustomerFavoritesScreen(
                        repository = repository,
                        onProductClick = { detailedProduct = it },
                        onExploreMenu = { currentTab = CustomerTab.MENU }
                    )
                }

                CustomerTab.CART -> {
                    CustomerCartScreen(
                        repository = repository,
                        fulfilmentType = fulfilmentType,
                        onFulfilmentChange = { fulfilmentType = it },
                        onExploreMenu = { currentTab = CustomerTab.MENU },
                        onProceedToCheckout = { showCheckoutModal = true }
                    )
                }

                CustomerTab.PROFILE -> {
                    CustomerProfileScreen(
                        repository = repository,
                        onOpenAdmin = onOpenAdmin
                    )
                }
            }
        }
    }

    // Product Customization Modal
    detailedProduct?.let { product ->
        ProductCustomizationDialog(
            product = product,
            onDismiss = { detailedProduct = null },
            onAddToCart = { qty, options, instructions ->
                repository.addToCart(product, qty, options, instructions)
            }
        )
    }

    // Checkout Modal
    if (showCheckoutModal) {
        CheckoutDialog(
            repository = repository,
            fulfilmentType = fulfilmentType,
            appliedCoupon = appliedCouponFromOffers,
            onDismiss = { showCheckoutModal = false },
            onOrderHandedOff = { order ->
                showCheckoutModal = false
                lastHandedOffOrder = order
            }
        )
    }

    // Order Handed Off Success Dialog
    lastHandedOffOrder?.let { order ->
        OrderHandedOffDialog(
            order = order,
            onDismiss = { lastHandedOffOrder = null },
            onViewHistory = {
                lastHandedOffOrder = null
                currentTab = CustomerTab.PROFILE
            }
        )
    }
}
