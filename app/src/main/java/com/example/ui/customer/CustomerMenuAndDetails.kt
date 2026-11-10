package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.CustomerRepository
import com.example.model.Category
import com.example.model.CustomizationGroup
import com.example.model.CustomizationOption
import com.example.model.FoodType
import com.example.model.Product
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.FoodCard
import com.example.ui.components.FoodTypeBadge
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
fun CustomerMenuScreen(
    repository: CustomerRepository,
    initialCategoryId: String? = null,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by repository.categories.collectAsState()
    val products by repository.products.collectAsState()
    val favorites by repository.favorites.collectAsState()
    val cart by repository.cart.collectAsState()

    var selectedCategoryId by remember(initialCategoryId) { mutableStateOf(initialCategoryId) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFoodType by remember { mutableStateOf<FoodType?>(null) } // null = all

    val enabledCategories = remember(categories) { categories.filter { it.isEnabled } }

    val filteredProducts = remember(products, selectedCategoryId, searchQuery, selectedFoodType) {
        products.filter { product ->
            val matchesCategory = selectedCategoryId == null || product.categoryId == selectedCategoryId
            val matchesSearch = searchQuery.isBlank() ||
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true)
            val matchesFoodType = selectedFoodType == null || product.foodType == selectedFoodType
            matchesCategory && matchesSearch && matchesFoodType
        }.sortedBy { it.displayOrder }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // Search Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Rajput Menu",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = RajputTextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("menu_search_input"),
                    placeholder = { Text("Search by dish name or ingredients...", fontSize = 13.sp) },
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
                        focusedContainerColor = RajputLightGrey,
                        unfocusedContainerColor = RajputLightGrey
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Veg / Non-Veg Quick Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = selectedFoodType == null,
                        onClick = { selectedFoodType = null },
                        label = { Text("All Dishes", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RajputRed,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = selectedFoodType == FoodType.VEG,
                        onClick = {
                            selectedFoodType = if (selectedFoodType == FoodType.VEG) null else FoodType.VEG
                        },
                        leadingIcon = { FoodTypeBadge(foodType = FoodType.VEG, size = 12.dp) },
                        label = { Text("Pure Veg", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RajputSuccess,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = selectedFoodType == FoodType.NON_VEG,
                        onClick = {
                            selectedFoodType = if (selectedFoodType == FoodType.NON_VEG) null else FoodType.NON_VEG
                        },
                        leadingIcon = { FoodTypeBadge(foodType = FoodType.NON_VEG, size = 12.dp) },
                        label = { Text("Non-Veg", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RajputDarkRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Horizontal Category Tabs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 6.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryPill(
                    name = "All Items",
                    isSelected = selectedCategoryId == null,
                    onClick = { selectedCategoryId = null }
                )
            }
            items(enabledCategories) { category ->
                CategoryPill(
                    name = category.name,
                    isSelected = selectedCategoryId == category.id,
                    onClick = { selectedCategoryId = category.id }
                )
            }
        }

        HorizontalDivider(color = RajputBorder, thickness = 0.5.dp)

        // Products List
        if (filteredProducts.isEmpty()) {
            EmptyPlaceholder(
                title = "No dishes found",
                message = "Try searching for something else or clear the filters.",
                actionLabel = "Clear Filters",
                onActionClick = {
                    searchQuery = ""
                    selectedCategoryId = null
                    selectedFoodType = null
                },
                modifier = Modifier.padding(top = 40.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${filteredProducts.size} items available",
                        fontSize = 12.sp,
                        color = RajputTextMuted,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                items(filteredProducts, key = { it.id }) { product ->
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
                        onToggleFavorite = { repository.toggleFavorite(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryPill(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) RajputRed else RajputLightGrey,
        border = BorderStroke(1.dp, if (isSelected) RajputRed else RajputBorder),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else RajputTextDark,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

/**
 * Product Details & Customization Dialog
 */
@Composable
fun ProductCustomizationDialog(
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: (quantity: Int, selectedOptions: List<CustomizationOption>, instructions: String) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var specialInstructions by remember { mutableStateOf("") }

    // Maintain selected options per customization group
    val selectedOptions = remember {
        mutableStateListOf<CustomizationOption>().apply {
            // Pick default required first options
            product.customizationGroups.forEach { group ->
                if (group.isRequired && group.options.isNotEmpty()) {
                    add(group.options.first())
                }
            }
        }
    }

    val additionalPrice = selectedOptions.sumOf { it.additionalPricePaise }
    val unitPrice = product.pricePaise + additionalPrice
    val totalPrice = unitPrice * quantity

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .testTag("product_details_dialog"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                // Header Image Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(RajputDarkRed, RajputCharcoal)
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rajput_logo),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }

                    if (product.isBestseller) {
                        Surface(
                            color = RajputGold,
                            shape = RoundedCornerShape(bottomEnd = 10.dp),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Text(
                                text = "🔥 BESTSELLER",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FoodTypeBadge(foodType = product.foodType)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (product.foodType == FoodType.VEG) "Vegetarian" else "Non-Vegetarian",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (product.foodType == FoodType.VEG) RajputSuccess else RajputDarkRed
                            )
                        }

                        Text(
                            text = "⏱️ ${product.prepTimeMinutes} mins",
                            fontSize = 12.sp,
                            color = RajputTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = RajputTextDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    PriceDisplay(
                        pricePaise = product.pricePaise,
                        originalPricePaise = product.originalPricePaise,
                        priceFontSize = 18
                    )

                    if (product.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.description,
                            fontSize = 13.sp,
                            color = RajputTextMuted,
                            lineHeight = 18.sp
                        )
                    }

                    // Ingredients tags
                    if (product.ingredients.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Ingredients:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = RajputTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(product.ingredients) { ing ->
                                Surface(
                                    color = RajputLightGrey,
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, RajputBorder)
                                ) {
                                    Text(
                                        text = ing,
                                        fontSize = 11.sp,
                                        color = RajputTextDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Customization Groups
                    if (product.customizationGroups.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = RajputBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        product.customizationGroups.forEach { group ->
                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = group.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = RajputTextDark
                                    )
                                    if (group.isRequired) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "*Required", color = RajputRed, fontSize = 11.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                group.options.forEach { option ->
                                    val isChosen = selectedOptions.any { it.id == option.id }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (group.isMultiSelect) {
                                                    if (isChosen) {
                                                        selectedOptions.removeAll { it.id == option.id }
                                                    } else {
                                                        selectedOptions.add(option)
                                                    }
                                                } else {
                                                    // Single select: remove others from this group, add this
                                                    val groupOptionIds = group.options.map { it.id }
                                                    selectedOptions.removeAll { groupOptionIds.contains(it.id) }
                                                    selectedOptions.add(option)
                                                }
                                            }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (group.isMultiSelect) {
                                                Checkbox(
                                                    checked = isChosen,
                                                    onCheckedChange = { checked ->
                                                        if (checked) selectedOptions.add(option)
                                                        else selectedOptions.removeAll { it.id == option.id }
                                                    },
                                                    colors = CheckboxDefaults.colors(checkedColor = RajputRed)
                                                )
                                            } else {
                                                RadioButton(
                                                    selected = isChosen,
                                                    onClick = {
                                                        val groupOptionIds = group.options.map { it.id }
                                                        selectedOptions.removeAll { groupOptionIds.contains(it.id) }
                                                        selectedOptions.add(option)
                                                    },
                                                    colors = RadioButtonDefaults.colors(selectedColor = RajputRed)
                                                )
                                            }
                                            Text(
                                                text = option.name,
                                                fontSize = 13.sp,
                                                color = RajputTextDark
                                            )
                                        }

                                        if (option.additionalPricePaise > 0L) {
                                            Text(
                                                text = "+₹${option.additionalPricePaise / 100}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = RajputDarkRed
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = RajputBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Special Cooking Instructions
                    OutlinedTextField(
                        value = specialInstructions,
                        onValueChange = { specialInstructions = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("special_instructions_input"),
                        label = { Text("Special cooking note (optional)", fontSize = 12.sp) },
                        placeholder = { Text("e.g. Less spicy, extra sauce, well toasted", fontSize = 12.sp) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RajputRed,
                            unfocusedBorderColor = RajputBorder
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quantity Stepper and Add To Cart
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Qty:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = RajputTextDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = RajputLightGrey,
                                border = BorderStroke(1.dp, RajputBorder)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (quantity > 1) quantity-- },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "$quantity",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(
                                        onClick = { if (quantity < 20) quantity++ },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                onAddToCart(quantity, selectedOptions.toList(), specialInstructions)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("confirm_add_to_cart_button")
                        ) {
                            Text(
                                text = "Add Item • ₹${totalPrice / 100}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
