package com.example.ui.customer

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CustomerRepository
import com.example.model.Combo
import com.example.model.Offer
import com.example.model.Product
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.FoodCard
import com.example.ui.components.PriceDisplay
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
fun CustomerOffersScreen(
    repository: CustomerRepository,
    onApplyOfferToCart: (String) -> Unit,
    onExploreMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offers by repository.offers.collectAsState()
    val combos by repository.combos.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Offers & Coupons, 1: Royal Combos

    val activeOffers = remember(offers) { offers.filter { it.isEnabled } }
    val activeCombos = remember(combos) { combos.filter { it.isAvailable } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // Tab Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Deals & Offers",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = RajputTextDark,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = RajputRed
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Coupons (${activeOffers.size})",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Combos (${activeCombos.size})",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }
        }

        if (selectedTab == 0) {
            if (activeOffers.isEmpty()) {
                EmptyPlaceholder(
                    title = "No Active Offers",
                    message = "Check back soon for festive discounts and royal promo codes!",
                    actionLabel = "Browse Menu",
                    onActionClick = onExploreMenu,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activeOffers) { offer ->
                        OfferCardItem(
                            offer = offer,
                            onCopyCode = {
                                clipboardManager.setText(AnnotatedString(offer.code))
                            },
                            onApply = { onApplyOfferToCart(offer.code) }
                        )
                    }
                }
            }
        } else {
            if (activeCombos.isEmpty()) {
                EmptyPlaceholder(
                    title = "No Combos Available",
                    message = "Check our standard dishes in the menu.",
                    actionLabel = "Browse Menu",
                    onActionClick = onExploreMenu,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(activeCombos) { combo ->
                        DetailedComboCard(
                            combo = combo,
                            onAddToCart = { repository.addComboToCart(combo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OfferCardItem(
    offer: Offer,
    onCopyCode: () -> Unit,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFECACA))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, RajputRed)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = offer.code,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = RajputRed,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = RajputRed,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable(onClick = onCopyCode)
                        )
                    }
                }

                if (offer.validUntil.isNotBlank()) {
                    Text(
                        text = "Valid: ${offer.validUntil}",
                        fontSize = 11.sp,
                        color = RajputTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = offer.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = RajputTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = offer.description,
                fontSize = 12.sp,
                color = RajputTextMuted,
                lineHeight = 16.sp
            )

            if (offer.minOrderPaise > 0L) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Applicable on orders above ₹${offer.minOrderPaise / 100}",
                    fontSize = 11.sp,
                    color = RajputDarkRed,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onApply,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Apply to Cart", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailedComboCard(
    combo: Combo,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                        text = "SPECIAL COMBO MEAL",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = combo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = RajputTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = combo.description,
                fontSize = 12.sp,
                color = RajputTextMuted,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriceDisplay(
                    pricePaise = combo.pricePaise,
                    originalPricePaise = combo.originalPricePaise,
                    priceFontSize = 17
                )

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("ADD COMBO +", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Favorites Screen
 */
@Composable
fun CustomerFavoritesScreen(
    repository: CustomerRepository,
    onProductClick: (Product) -> Unit,
    onExploreMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by repository.favorites.collectAsState()
    val products by repository.products.collectAsState()
    val cart by repository.cart.collectAsState()

    val favoriteProducts = remember(favorites, products) {
        products.filter { favorites.contains(it.id) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = RajputRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "My Favorites",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = RajputTextDark
                    )
                    Text(
                        text = "${favoriteProducts.size} dishes saved",
                        fontSize = 12.sp,
                        color = RajputTextMuted
                    )
                }
            }
        }

        if (favoriteProducts.isEmpty()) {
            EmptyPlaceholder(
                title = "No Favorites Yet",
                message = "Tap the heart icon on any dish to save it here for fast ordering!",
                actionLabel = "Explore Menu",
                onActionClick = onExploreMenu,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favoriteProducts, key = { it.id }) { product ->
                    val cartQty = cart.filter { it.product.id == product.id }.sumOf { it.quantity }

                    FoodCard(
                        product = product,
                        isFavorite = true,
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
                        onToggleFavorite = { repository.toggleFavorite(product.id) }
                    )
                }
            }
        }
    }
}
