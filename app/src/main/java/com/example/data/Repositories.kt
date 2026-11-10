package com.example.data

import android.content.Context
import com.example.model.CartItem
import com.example.model.CataloguePayload
import com.example.model.Category
import com.example.model.Combo
import com.example.model.CustomerOrder
import com.example.model.CustomizationOption
import com.example.model.Offer
import com.example.model.OrderFulfilmentType
import com.example.model.Product
import com.example.model.RestaurantSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Isolated Admin Repository managing catalogue files for the Admin App.
 * Strict data isolation: changes here do not alter CustomerRepository until exported and imported.
 */
class AdminRepository(private val context: Context) {
    private val adminFile = File(context.filesDir, "admin_catalogue.json")
    private val backupFile = File(context.filesDir, "admin_catalogue_backup.json")

    private val _settings = MutableStateFlow(DefaultData.defaultSettings)
    val settings: StateFlow<RestaurantSettings> = _settings.asStateFlow()

    private val _categories = MutableStateFlow(DefaultData.defaultCategories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow(DefaultData.defaultProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _offers = MutableStateFlow(DefaultData.defaultOffers)
    val offers: StateFlow<List<Offer>> = _offers.asStateFlow()

    private val _combos = MutableStateFlow(DefaultData.defaultCombos)
    val combos: StateFlow<List<Combo>> = _combos.asStateFlow()

    init {
        loadFromDisk()
    }

    private fun loadFromDisk() {
        try {
            if (adminFile.exists()) {
                val jsonStr = adminFile.readText()
                val payload = CataloguePayload.fromJson(jsonStr)
                _settings.value = payload.settings
                _categories.value = payload.categories
                _products.value = payload.products
                _offers.value = payload.offers
                _combos.value = payload.combos
            } else {
                saveToDisk()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToDisk() {
        try {
            val payload = CataloguePayload(
                version = 1,
                exportedAt = System.currentTimeMillis(),
                settings = _settings.value,
                categories = _categories.value,
                products = _products.value,
                offers = _offers.value,
                combos = _combos.value
            )
            adminFile.writeText(payload.toJson())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exportCatalogueJson(): String {
        val payload = CataloguePayload(
            version = 1,
            exportedAt = System.currentTimeMillis(),
            settings = _settings.value,
            categories = _categories.value,
            products = _products.value,
            offers = _offers.value,
            combos = _combos.value
        )
        return payload.toJson()
    }

    fun importCatalogueJson(jsonStr: String): Result<String> {
        return try {
            val payload = CataloguePayload.fromJson(jsonStr)
            if (payload.products.isEmpty() && payload.categories.isEmpty()) {
                return Result.failure(IllegalArgumentException("Invalid catalogue: No products or categories found."))
            }
            // Backup existing
            if (adminFile.exists()) {
                backupFile.writeText(adminFile.readText())
            }
            _settings.value = payload.settings
            _categories.value = payload.categories
            _products.value = payload.products
            _offers.value = payload.offers
            _combos.value = payload.combos
            saveToDisk()
            Result.success("Catalogue imported successfully (${payload.products.size} products, ${payload.categories.size} categories).")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resetToDefault(): Boolean {
        return try {
            _settings.value = DefaultData.defaultSettings
            _categories.value = DefaultData.defaultCategories
            _products.value = DefaultData.defaultProducts
            _offers.value = DefaultData.defaultOffers
            _combos.value = DefaultData.defaultCombos
            saveToDisk()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Product Management
    fun saveProduct(product: Product) {
        val current = _products.value.toMutableList()
        val index = current.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            current[index] = product
        } else {
            current.add(0, product)
        }
        _products.value = current
        saveToDisk()
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filter { it.id != productId }
        saveToDisk()
    }

    fun toggleProductAvailability(productId: String) {
        _products.value = _products.value.map {
            if (it.id == productId) it.copy(isAvailable = !it.isAvailable) else it
        }
        saveToDisk()
    }

    fun toggleProductBestseller(productId: String) {
        _products.value = _products.value.map {
            if (it.id == productId) it.copy(isBestseller = !it.isBestseller) else it
        }
        saveToDisk()
    }

    // Category Management
    fun saveCategory(category: Category) {
        val current = _categories.value.toMutableList()
        val index = current.indexOfFirst { it.id == category.id }
        if (index >= 0) {
            current[index] = category
        } else {
            current.add(category)
        }
        _categories.value = current
        saveToDisk()
    }

    fun deleteCategory(categoryId: String): Result<Boolean> {
        val productsInCat = _products.value.count { it.categoryId == categoryId }
        if (productsInCat > 0) {
            return Result.failure(IllegalStateException("Cannot delete category containing $productsInCat products. Please reassign or delete the products first."))
        }
        _categories.value = _categories.value.filter { it.id != categoryId }
        saveToDisk()
        return Result.success(true)
    }

    fun toggleCategoryEnabled(categoryId: String) {
        _categories.value = _categories.value.map {
            if (it.id == categoryId) it.copy(isEnabled = !it.isEnabled) else it
        }
        saveToDisk()
    }

    // Offer Management
    fun saveOffer(offer: Offer) {
        val current = _offers.value.toMutableList()
        val index = current.indexOfFirst { it.id == offer.id }
        if (index >= 0) {
            current[index] = offer
        } else {
            current.add(offer)
        }
        _offers.value = current
        saveToDisk()
    }

    fun deleteOffer(offerId: String) {
        _offers.value = _offers.value.filter { it.id != offerId }
        saveToDisk()
    }

    fun toggleOfferEnabled(offerId: String) {
        _offers.value = _offers.value.map {
            if (it.id == offerId) it.copy(isEnabled = !it.isEnabled) else it
        }
        saveToDisk()
    }

    // Combo Management
    fun saveCombo(combo: Combo) {
        val current = _combos.value.toMutableList()
        val index = current.indexOfFirst { it.id == combo.id }
        if (index >= 0) {
            current[index] = combo
        } else {
            current.add(combo)
        }
        _combos.value = current
        saveToDisk()
    }

    fun deleteCombo(comboId: String) {
        _combos.value = _combos.value.filter { it.id != comboId }
        saveToDisk()
    }

    fun toggleComboAvailable(comboId: String) {
        _combos.value = _combos.value.map {
            if (it.id == comboId) it.copy(isAvailable = !it.isAvailable) else it
        }
        saveToDisk()
    }

    // Settings
    fun updateSettings(newSettings: RestaurantSettings) {
        _settings.value = newSettings
        saveToDisk()
    }
}

/**
 * Isolated Customer Repository for the Customer App.
 * Manages catalogue, cart, favorites, order history, and preferences.
 */
class CustomerRepository(private val context: Context) {
    private val customerMenuFile = File(context.filesDir, "customer_catalogue.json")
    private val customerBackupFile = File(context.filesDir, "customer_catalogue_backup.json")
    private val favoritesFile = File(context.filesDir, "customer_favorites.json")
    private val orderHistoryFile = File(context.filesDir, "customer_orders.json")
    private val profileFile = File(context.filesDir, "customer_profile.json")

    private val _settings = MutableStateFlow(DefaultData.defaultSettings)
    val settings: StateFlow<RestaurantSettings> = _settings.asStateFlow()

    private val _categories = MutableStateFlow(DefaultData.defaultCategories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow(DefaultData.defaultProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _offers = MutableStateFlow(DefaultData.defaultOffers)
    val offers: StateFlow<List<Offer>> = _offers.asStateFlow()

    private val _combos = MutableStateFlow(DefaultData.defaultCombos)
    val combos: StateFlow<List<Combo>> = _combos.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _orderHistory = MutableStateFlow<List<CustomerOrder>>(emptyList())
    val orderHistory: StateFlow<List<CustomerOrder>> = _orderHistory.asStateFlow()

    private val _savedName = MutableStateFlow("")
    val savedName: StateFlow<String> = _savedName.asStateFlow()

    private val _savedPhone = MutableStateFlow("")
    val savedPhone: StateFlow<String> = _savedPhone.asStateFlow()

    private val _savedAddresses = MutableStateFlow<List<String>>(emptyList())
    val savedAddresses: StateFlow<List<String>> = _savedAddresses.asStateFlow()

    init {
        loadCatalogue()
        loadFavorites()
        loadOrderHistory()
        loadProfile()
    }

    private fun loadCatalogue() {
        try {
            if (customerMenuFile.exists()) {
                val jsonStr = customerMenuFile.readText()
                val payload = CataloguePayload.fromJson(jsonStr)
                _settings.value = payload.settings
                _categories.value = payload.categories
                _products.value = payload.products
                _offers.value = payload.offers
                _combos.value = payload.combos
            } else {
                saveCatalogue()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveCatalogue() {
        try {
            val payload = CataloguePayload(
                version = 1,
                exportedAt = System.currentTimeMillis(),
                settings = _settings.value,
                categories = _categories.value,
                products = _products.value,
                offers = _offers.value,
                combos = _combos.value
            )
            customerMenuFile.writeText(payload.toJson())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun importUpdatedCatalogue(jsonStr: String): Result<String> {
        return try {
            val payload = CataloguePayload.fromJson(jsonStr)
            if (payload.products.isEmpty() && payload.categories.isEmpty()) {
                return Result.failure(IllegalArgumentException("Invalid catalogue file: No items found."))
            }
            if (customerMenuFile.exists()) {
                customerBackupFile.writeText(customerMenuFile.readText())
            }
            _settings.value = payload.settings
            _categories.value = payload.categories
            _products.value = payload.products
            _offers.value = payload.offers
            _combos.value = payload.combos
            saveCatalogue()
            Result.success("Catalogue updated successfully with ${payload.products.size} items.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cart Management
    fun addToCart(
        product: Product,
        quantity: Int = 1,
        selectedOptions: List<CustomizationOption> = emptyList(),
        specialInstructions: String = ""
    ) {
        val current = _cart.value.toMutableList()
        // If exact item with same options and notes exists, increment quantity
        val existingIndex = current.indexOfFirst {
            it.product.id == product.id &&
            it.selectedOptions.map { o -> o.id }.sorted() == selectedOptions.map { o -> o.id }.sorted() &&
            it.specialInstructions == specialInstructions
        }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(
                CartItem(
                    product = product,
                    quantity = quantity,
                    selectedOptions = selectedOptions,
                    specialInstructions = specialInstructions
                )
            )
        }
        _cart.value = current
    }

    fun addComboToCart(combo: Combo) {
        // Pseudo product for combo
        val comboProduct = Product(
            id = combo.id,
            name = combo.name,
            categoryId = "cat_combos",
            description = combo.description,
            pricePaise = combo.pricePaise,
            originalPricePaise = combo.originalPricePaise,
            foodType = com.example.model.FoodType.VEG,
            isAvailable = combo.isAvailable
        )
        addToCart(comboProduct, 1)
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeCartItem(cartItemId)
        } else {
            _cart.value = _cart.value.map {
                if (it.cartItemId == cartItemId) it.copy(quantity = newQuantity) else it
            }
        }
    }

    fun removeCartItem(cartItemId: String) {
        _cart.value = _cart.value.filter { it.cartItemId != cartItemId }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Favorites
    private fun loadFavorites() {
        try {
            if (favoritesFile.exists()) {
                val jsonStr = favoritesFile.readText()
                val arr = JSONArray(jsonStr)
                val set = mutableSetOf<String>()
                for (i in 0 until arr.length()) {
                    set.add(arr.getString(i))
                }
                _favorites.value = set
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleFavorite(productId: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _favorites.value = current
        try {
            val arr = JSONArray()
            current.forEach { arr.put(it) }
            favoritesFile.writeText(arr.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isFavorite(productId: String): Boolean = _favorites.value.contains(productId)

    // Order History
    private fun loadOrderHistory() {
        try {
            if (orderHistoryFile.exists()) {
                val jsonStr = orderHistoryFile.readText()
                val arr = JSONArray(jsonStr)
                val list = mutableListOf<CustomerOrder>()
                for (i in 0 until arr.length()) {
                    list.add(CustomerOrder.fromJson(arr.getJSONObject(i)))
                }
                _orderHistory.value = list
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveOrderRecord(order: CustomerOrder) {
        val current = _orderHistory.value.toMutableList()
        current.add(0, order)
        _orderHistory.value = current
        try {
            val arr = JSONArray()
            current.forEach { arr.put(it.toJson()) }
            orderHistoryFile.writeText(arr.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearOrderHistory() {
        _orderHistory.value = emptyList()
        if (orderHistoryFile.exists()) {
            orderHistoryFile.delete()
        }
    }

    // Profile & Addresses
    private fun loadProfile() {
        try {
            if (profileFile.exists()) {
                val obj = JSONObject(profileFile.readText())
                _savedName.value = obj.optString("name", "")
                _savedPhone.value = obj.optString("phone", "")
                val addrArr = obj.optJSONArray("addresses")
                val addrs = mutableListOf<String>()
                if (addrArr != null) {
                    for (i in 0 until addrArr.length()) {
                        addrs.add(addrArr.getString(i))
                    }
                }
                _savedAddresses.value = addrs
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveCustomerDetails(name: String, phone: String) {
        _savedName.value = name
        _savedPhone.value = phone
        persistProfile()
    }

    fun addSavedAddress(address: String) {
        if (address.isBlank()) return
        val current = _savedAddresses.value.toMutableList()
        if (!current.contains(address)) {
            current.add(0, address)
            _savedAddresses.value = current
            persistProfile()
        }
    }

    fun removeSavedAddress(address: String) {
        _savedAddresses.value = _savedAddresses.value.filter { it != address }
        persistProfile()
    }

    private fun persistProfile() {
        try {
            val obj = JSONObject().apply {
                put("name", _savedName.value)
                put("phone", _savedPhone.value)
                val arr = JSONArray()
                _savedAddresses.value.forEach { arr.put(it) }
                put("addresses", arr)
            }
            profileFile.writeText(obj.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearAllLocalData() {
        clearCart()
        _favorites.value = emptySet()
        if (favoritesFile.exists()) favoritesFile.delete()
        clearOrderHistory()
        _savedName.value = ""
        _savedPhone.value = ""
        _savedAddresses.value = emptyList()
        if (profileFile.exists()) profileFile.delete()
    }
}

/**
 * Helper to construct WhatsApp messages and Phone Call intents
 * in strict compliance with Section 14 of the PRD.
 */
object OrderHandoffHelper {

    fun buildWhatsAppMessage(
        restaurantName: String,
        customerName: String,
        customerPhone: String,
        fulfilmentType: OrderFulfilmentType,
        items: List<CartItem>,
        subtotalPaise: Long,
        deliveryFeePaise: Long,
        discountPaise: Long,
        discountCode: String,
        grandTotalPaise: Long,
        deliveryAddress: String,
        landmark: String,
        pickupTime: String,
        specialInstructions: String
    ): String {
        val sb = StringBuilder()
        sb.append("Hello ").append(restaurantName).append("!\n\n")
        sb.append("I would like to place an order.\n\n")
        sb.append("Customer: ").append(customerName.trim()).append("\n")
        sb.append("Phone: ").append(customerPhone.trim()).append("\n")
        sb.append("Order Type: ").append(fulfilmentType.label).append("\n\n")

        sb.append("ITEMS\n")
        items.forEachIndexed { index, item ->
            sb.append("${index + 1}. ").append(item.product.name)
            if (item.selectedOptions.isNotEmpty()) {
                val opts = item.selectedOptions.joinToString(", ") { it.name }
                sb.append(" (").append(opts).append(")")
            }
            sb.append(" x ").append(item.quantity)
            sb.append(" - ₹").append(item.totalPaise / 100)
            if (item.specialInstructions.isNotBlank()) {
                sb.append(" [Note: ").append(item.specialInstructions.trim()).append("]")
            }
            sb.append("\n")
        }

        sb.append("\nSubtotal: ₹").append(subtotalPaise / 100).append("\n")
        if (fulfilmentType == OrderFulfilmentType.DELIVERY) {
            sb.append("Delivery Charge: ₹").append(deliveryFeePaise / 100).append("\n")
        }
        if (discountPaise > 0L) {
            sb.append("Discount: ₹").append(discountPaise / 100)
            if (discountCode.isNotBlank()) sb.append(" (").append(discountCode).append(")")
            sb.append("\n")
        }
        sb.append("Grand Total: ₹").append(grandTotalPaise / 100).append("\n\n")

        if (fulfilmentType == OrderFulfilmentType.DELIVERY) {
            sb.append("Delivery Address:\n").append(deliveryAddress.trim()).append("\n")
            if (landmark.isNotBlank()) {
                sb.append("Landmark: ").append(landmark.trim()).append("\n")
            }
            sb.append("\n")
        } else {
            if (pickupTime.isNotBlank()) {
                sb.append("Preferred Pickup Time: ").append(pickupTime.trim()).append("\n\n")
            }
        }

        if (specialInstructions.isNotBlank()) {
            sb.append("Instructions:\n").append(specialInstructions.trim()).append("\n\n")
        }

        sb.append("Please confirm my order.\n")
        sb.append("Thank you!")
        return sb.toString()
    }

    fun getWhatsAppUrl(phone: String, message: String): String {
        // Strip non-digits except leading +
        var cleaned = phone.replace(" ", "").replace("-", "")
        if (!cleaned.startsWith("+")) {
            if (cleaned.length == 10) {
                cleaned = "+91$cleaned"
            }
        }
        val encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.name())
        return "https://api.whatsapp.com/send?phone=$cleaned&text=$encodedMsg"
    }
}
