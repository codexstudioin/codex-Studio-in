package com.example.model

import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

enum class FoodType(val label: String) {
    VEG("veg"),
    NON_VEG("non-veg");

    companion object {
        fun from(value: String): FoodType =
            if (value.equals("non-veg", ignoreCase = true) || value.equals("nonveg", ignoreCase = true)) {
                NON_VEG
            } else {
                VEG
            }
    }
}

data class CustomizationOption(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val additionalPricePaise: Long = 0L // in paise (e.g. 2000 = ₹20)
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("additionalPricePaise", additionalPricePaise)
    }

    companion object {
        fun fromJson(json: JSONObject): CustomizationOption = CustomizationOption(
            id = json.optString("id", UUID.randomUUID().toString()),
            name = json.optString("name", ""),
            additionalPricePaise = json.optLong("additionalPricePaise", 0L)
        )
    }
}

data class CustomizationGroup(
    val id: String = UUID.randomUUID().toString(),
    val name: String, // e.g. "Size", "Spice Level", "Add Extra Cheese"
    val isMultiSelect: Boolean = false,
    val isRequired: Boolean = false,
    val options: List<CustomizationOption> = emptyList()
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("isMultiSelect", isMultiSelect)
        put("isRequired", isRequired)
        val arr = JSONArray()
        options.forEach { arr.put(it.toJson()) }
        put("options", arr)
    }

    companion object {
        fun fromJson(json: JSONObject): CustomizationGroup {
            val opts = mutableListOf<CustomizationOption>()
            val arr = json.optJSONArray("options")
            if (arr != null) {
                for (i in 0 until arr.length()) {
                    opts.add(CustomizationOption.fromJson(arr.getJSONObject(i)))
                }
            }
            return CustomizationGroup(
                id = json.optString("id", UUID.randomUUID().toString()),
                name = json.optString("name", ""),
                isMultiSelect = json.optBoolean("isMultiSelect", false),
                isRequired = json.optBoolean("isRequired", false),
                options = opts
            )
        }
    }
}

data class Product(
    val id: String = "prod_${System.currentTimeMillis()}",
    val name: String,
    val categoryId: String,
    val description: String = "",
    val pricePaise: Long = 0L, // in paise: 9900 = ₹99
    val originalPricePaise: Long = 0L, // 0 if no discount
    val imageAsset: String = "", // resource name or file URI
    val foodType: FoodType = FoodType.VEG,
    val isAvailable: Boolean = true,
    val isBestseller: Boolean = false,
    val displayOrder: Int = 1,
    val prepTimeMinutes: Int = 15,
    val ingredients: List<String> = emptyList(),
    val customizationGroups: List<CustomizationGroup> = emptyList()
) {
    val priceRupees: Long get() = pricePaise / 100
    val originalPriceRupees: Long get() = originalPricePaise / 100
    val hasDiscount: Boolean get() = originalPricePaise > pricePaise

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("categoryId", categoryId)
        put("description", description)
        put("pricePaise", pricePaise)
        put("originalPricePaise", originalPricePaise)
        put("imageAsset", imageAsset)
        put("foodType", foodType.label)
        put("isAvailable", isAvailable)
        put("isBestseller", isBestseller)
        put("displayOrder", displayOrder)
        put("prepTimeMinutes", prepTimeMinutes)
        val ingArr = JSONArray()
        ingredients.forEach { ingArr.put(it) }
        put("ingredients", ingArr)
        val cgArr = JSONArray()
        customizationGroups.forEach { cgArr.put(it.toJson()) }
        put("customizationGroups", cgArr)
    }

    companion object {
        fun fromJson(json: JSONObject): Product {
            val ings = mutableListOf<String>()
            val ingArr = json.optJSONArray("ingredients")
            if (ingArr != null) {
                for (i in 0 until ingArr.length()) {
                    ings.add(ingArr.getString(i))
                }
            }
            val cgs = mutableListOf<CustomizationGroup>()
            val cgArr = json.optJSONArray("customizationGroups")
            if (cgArr != null) {
                for (i in 0 until cgArr.length()) {
                    cgs.add(CustomizationGroup.fromJson(cgArr.getJSONObject(i)))
                }
            }
            return Product(
                id = json.optString("id", "prod_${UUID.randomUUID()}"),
                name = json.optString("name", "Item"),
                categoryId = json.optString("categoryId", ""),
                description = json.optString("description", ""),
                pricePaise = json.optLong("pricePaise", 0L),
                originalPricePaise = json.optLong("originalPricePaise", 0L),
                imageAsset = json.optString("imageAsset", ""),
                foodType = FoodType.from(json.optString("foodType", "veg")),
                isAvailable = json.optBoolean("isAvailable", true),
                isBestseller = json.optBoolean("isBestseller", false),
                displayOrder = json.optInt("displayOrder", 1),
                prepTimeMinutes = json.optInt("prepTimeMinutes", 15),
                ingredients = ings,
                customizationGroups = cgs
            )
        }
    }
}

data class Category(
    val id: String = "cat_${System.currentTimeMillis()}",
    val name: String,
    val description: String = "",
    val imageAsset: String = "",
    val isEnabled: Boolean = true,
    val displayOrder: Int = 1
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("description", description)
        put("imageAsset", imageAsset)
        put("isEnabled", isEnabled)
        put("displayOrder", displayOrder)
    }

    companion object {
        fun fromJson(json: JSONObject): Category = Category(
            id = json.optString("id", "cat_${UUID.randomUUID()}"),
            name = json.optString("name", "Category"),
            description = json.optString("description", ""),
            imageAsset = json.optString("imageAsset", ""),
            isEnabled = json.optBoolean("isEnabled", true),
            displayOrder = json.optInt("displayOrder", 1)
        )
    }
}

enum class DiscountType(val label: String) {
    PERCENTAGE("percentage"),
    FLAT("flat");

    companion object {
        fun from(value: String): DiscountType =
            if (value.equals("percentage", ignoreCase = true)) PERCENTAGE else FLAT
    }
}

data class Offer(
    val id: String = "offer_${System.currentTimeMillis()}",
    val code: String, // e.g. "RAJPUT50"
    val title: String,
    val description: String,
    val discountType: DiscountType = DiscountType.FLAT,
    val discountValue: Long = 0L, // 5000 paise (₹50) or 20 (for 20%)
    val minOrderPaise: Long = 0L, // in paise
    val maxDiscountPaise: Long = 0L, // in paise (e.g. 10000 = ₹100 max)
    val validUntil: String = "",
    val isEnabled: Boolean = true
) {
    fun calculateDiscountPaise(subtotalPaise: Long): Long {
        if (!isEnabled || subtotalPaise < minOrderPaise) return 0L
        val disc = when (discountType) {
            DiscountType.FLAT -> discountValue
            DiscountType.PERCENTAGE -> (subtotalPaise * discountValue) / 100
        }
        return if (maxDiscountPaise > 0L) disc.coerceAtMost(maxDiscountPaise) else disc
    }

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("code", code)
        put("title", title)
        put("description", description)
        put("discountType", discountType.label)
        put("discountValue", discountValue)
        put("minOrderPaise", minOrderPaise)
        put("maxDiscountPaise", maxDiscountPaise)
        put("validUntil", validUntil)
        put("isEnabled", isEnabled)
    }

    companion object {
        fun fromJson(json: JSONObject): Offer = Offer(
            id = json.optString("id", "offer_${UUID.randomUUID()}"),
            code = json.optString("code", json.optString("id", "OFFER")),
            title = json.optString("title", "Special Discount"),
            description = json.optString("description", ""),
            discountType = DiscountType.from(json.optString("discountType", "flat")),
            discountValue = json.optLong("discountValue", 0L),
            minOrderPaise = json.optLong("minOrderPaise", 0L),
            maxDiscountPaise = json.optLong("maxDiscountPaise", 0L),
            validUntil = json.optString("validUntil", ""),
            isEnabled = json.optBoolean("isEnabled", true)
        )
    }
}

data class Combo(
    val id: String = "combo_${System.currentTimeMillis()}",
    val name: String,
    val description: String,
    val pricePaise: Long, // in paise
    val originalPricePaise: Long, // in paise
    val includedItemIds: List<String> = emptyList(),
    val imageAsset: String = "",
    val isAvailable: Boolean = true
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("description", description)
        put("pricePaise", pricePaise)
        put("originalPricePaise", originalPricePaise)
        val arr = JSONArray()
        includedItemIds.forEach { arr.put(it) }
        put("includedItemIds", arr)
        put("imageAsset", imageAsset)
        put("isAvailable", isAvailable)
    }

    companion object {
        fun fromJson(json: JSONObject): Combo {
            val items = mutableListOf<String>()
            val arr = json.optJSONArray("includedItemIds")
            if (arr != null) {
                for (i in 0 until arr.length()) {
                    items.add(arr.getString(i))
                }
            }
            return Combo(
                id = json.optString("id", "combo_${UUID.randomUUID()}"),
                name = json.optString("name", "Royal Combo"),
                description = json.optString("description", ""),
                pricePaise = json.optLong("pricePaise", 0L),
                originalPricePaise = json.optLong("originalPricePaise", 0L),
                includedItemIds = items,
                imageAsset = json.optString("imageAsset", ""),
                isAvailable = json.optBoolean("isAvailable", true)
            )
        }
    }
}

data class RestaurantSettings(
    val restaurantName: String = "Rajput Fast Food",
    val tagline: String = "Royal Taste, Lightning Fast!",
    val phone: String = "+91 98765 43210",
    val whatsapp: String = "+91 98765 43210",
    val address: String = "Near Royal Palace Chowk, Station Road, Jaipur, Rajasthan 302001",
    val openingHours: String = "11:00 AM – 11:30 PM (Mon – Sun)",
    val deliveryEnabled: Boolean = true,
    val takeawayEnabled: Boolean = true,
    val deliveryChargePaise: Long = 4000L, // ₹40
    val freeDeliveryThresholdPaise: Long = 49900L, // Free delivery above ₹499
    val minOrderPaise: Long = 14900L, // ₹149 minimum
    val aboutText: String = "Rajput Fast Food serves authentic royal taste fast-food crafted with the finest ingredients, premium spices, and passion. From sizzling hot burgers to royal combos, enjoy swift delivery and pickup.",
    val adminPin: String = "1234"
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("restaurantName", restaurantName)
        put("tagline", tagline)
        put("phone", phone)
        put("whatsapp", whatsapp)
        put("address", address)
        put("openingHours", openingHours)
        put("deliveryEnabled", deliveryEnabled)
        put("takeawayEnabled", takeawayEnabled)
        put("deliveryChargePaise", deliveryChargePaise)
        put("freeDeliveryThresholdPaise", freeDeliveryThresholdPaise)
        put("minOrderPaise", minOrderPaise)
        put("aboutText", aboutText)
        put("adminPin", adminPin)
    }

    companion object {
        fun fromJson(json: JSONObject): RestaurantSettings = RestaurantSettings(
            restaurantName = json.optString("restaurantName", "Rajput Fast Food"),
            tagline = json.optString("tagline", "Royal Taste, Lightning Fast!"),
            phone = json.optString("phone", "+91 98765 43210"),
            whatsapp = json.optString("whatsapp", "+91 98765 43210"),
            address = json.optString("address", "Near Royal Palace Chowk, Station Road, Jaipur, Rajasthan 302001"),
            openingHours = json.optString("openingHours", "11:00 AM – 11:30 PM (Mon – Sun)"),
            deliveryEnabled = json.optBoolean("deliveryEnabled", true),
            takeawayEnabled = json.optBoolean("takeawayEnabled", true),
            deliveryChargePaise = json.optLong("deliveryChargePaise", 4000L),
            freeDeliveryThresholdPaise = json.optLong("freeDeliveryThresholdPaise", 49900L),
            minOrderPaise = json.optLong("minOrderPaise", 14900L),
            aboutText = json.optString("aboutText", "Rajput Fast Food"),
            adminPin = json.optString("adminPin", "1234")
        )
    }
}

// Versioned catalogue envelope for JSON export and import between Admin & Customer apps
data class CataloguePayload(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val settings: RestaurantSettings = RestaurantSettings(),
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val combos: List<Combo> = emptyList()
) {
    fun toJson(): String {
        val root = JSONObject().apply {
            put("version", version)
            put("exportedAt", exportedAt)
            put("settings", settings.toJson())

            val catArr = JSONArray()
            categories.forEach { catArr.put(it.toJson()) }
            put("categories", catArr)

            val prodArr = JSONArray()
            products.forEach { prodArr.put(it.toJson()) }
            put("products", prodArr)

            val offerArr = JSONArray()
            offers.forEach { offerArr.put(it.toJson()) }
            put("offers", offerArr)

            val comboArr = JSONArray()
            combos.forEach { comboArr.put(it.toJson()) }
            put("combos", comboArr)
        }
        return root.toString(2)
    }

    companion object {
        fun fromJson(jsonStr: String): CataloguePayload {
            val root = JSONObject(jsonStr)
            val version = root.optInt("version", 1)
            val exportedAt = root.optLong("exportedAt", System.currentTimeMillis())

            val settings = if (root.has("settings")) {
                RestaurantSettings.fromJson(root.getJSONObject("settings"))
            } else {
                RestaurantSettings()
            }

            val cats = mutableListOf<Category>()
            val catArr = root.optJSONArray("categories")
            if (catArr != null) {
                for (i in 0 until catArr.length()) {
                    cats.add(Category.fromJson(catArr.getJSONObject(i)))
                }
            }

            val prods = mutableListOf<Product>()
            val prodArr = root.optJSONArray("products")
            if (prodArr != null) {
                for (i in 0 until prodArr.length()) {
                    prods.add(Product.fromJson(prodArr.getJSONObject(i)))
                }
            }

            val offs = mutableListOf<Offer>()
            val offArr = root.optJSONArray("offers")
            if (offArr != null) {
                for (i in 0 until offArr.length()) {
                    offs.add(Offer.fromJson(offArr.getJSONObject(i)))
                }
            }

            val cmbs = mutableListOf<Combo>()
            val cmbArr = root.optJSONArray("combos")
            if (cmbArr != null) {
                for (i in 0 until cmbArr.length()) {
                    cmbs.add(Combo.fromJson(cmbArr.getJSONObject(i)))
                }
            }

            return CataloguePayload(
                version = version,
                exportedAt = exportedAt,
                settings = settings,
                categories = cats,
                products = prods,
                offers = offs,
                combos = cmbs
            )
        }
    }
}

// Shopping Cart & Order models
data class CartItem(
    val cartItemId: String = UUID.randomUUID().toString(),
    val product: Product,
    val quantity: Int = 1,
    val selectedOptions: List<CustomizationOption> = emptyList(),
    val specialInstructions: String = ""
) {
    val unitPricePaise: Long
        get() = product.pricePaise + selectedOptions.sumOf { it.additionalPricePaise }

    val totalPaise: Long
        get() = unitPricePaise * quantity

    fun toJson(): JSONObject = JSONObject().apply {
        put("cartItemId", cartItemId)
        put("productId", product.id)
        put("quantity", quantity)
        put("specialInstructions", specialInstructions)
        val opts = JSONArray()
        selectedOptions.forEach { opts.put(it.toJson()) }
        put("selectedOptions", opts)
    }
}

enum class OrderFulfilmentType(val label: String) {
    DELIVERY("Delivery"),
    TAKEAWAY("Takeaway")
}

data class CustomerOrder(
    val id: String = "RFF-${System.currentTimeMillis() % 1000000}",
    val timestamp: Long = System.currentTimeMillis(),
    val fulfilmentType: OrderFulfilmentType,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String = "",
    val landmark: String = "",
    val pickupTime: String = "",
    val specialInstructions: String = "",
    val itemsSummary: List<String>,
    val subtotalPaise: Long,
    val deliveryChargePaise: Long,
    val discountPaise: Long,
    val grandTotalPaise: Long,
    val appliedCoupon: String = "",
    val handoffMethod: String, // "WHATSAPP" or "PHONE"
    val status: String = "Prepared & Handed Off"
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("timestamp", timestamp)
        put("fulfilmentType", fulfilmentType.name)
        put("customerName", customerName)
        put("customerPhone", customerPhone)
        put("deliveryAddress", deliveryAddress)
        put("landmark", landmark)
        put("pickupTime", pickupTime)
        put("specialInstructions", specialInstructions)
        val arr = JSONArray()
        itemsSummary.forEach { arr.put(it) }
        put("itemsSummary", arr)
        put("subtotalPaise", subtotalPaise)
        put("deliveryChargePaise", deliveryChargePaise)
        put("discountPaise", discountPaise)
        put("grandTotalPaise", grandTotalPaise)
        put("appliedCoupon", appliedCoupon)
        put("handoffMethod", handoffMethod)
        put("status", status)
    }

    companion object {
        fun fromJson(json: JSONObject): CustomerOrder {
            val summs = mutableListOf<String>()
            val arr = json.optJSONArray("itemsSummary")
            if (arr != null) {
                for (i in 0 until arr.length()) {
                    summs.add(arr.getString(i))
                }
            }
            return CustomerOrder(
                id = json.optString("id", "RFF-001"),
                timestamp = json.optLong("timestamp", System.currentTimeMillis()),
                fulfilmentType = try {
                    OrderFulfilmentType.valueOf(json.optString("fulfilmentType", "DELIVERY"))
                } catch (e: Exception) {
                    OrderFulfilmentType.DELIVERY
                },
                customerName = json.optString("customerName", ""),
                customerPhone = json.optString("customerPhone", ""),
                deliveryAddress = json.optString("deliveryAddress", ""),
                landmark = json.optString("landmark", ""),
                pickupTime = json.optString("pickupTime", ""),
                specialInstructions = json.optString("specialInstructions", ""),
                itemsSummary = summs,
                subtotalPaise = json.optLong("subtotalPaise", 0L),
                deliveryChargePaise = json.optLong("deliveryChargePaise", 0L),
                discountPaise = json.optLong("discountPaise", 0L),
                grandTotalPaise = json.optLong("grandTotalPaise", 0L),
                appliedCoupon = json.optString("appliedCoupon", ""),
                handoffMethod = json.optString("handoffMethod", "WHATSAPP"),
                status = json.optString("status", "Prepared & Handed Off")
            )
        }
    }
}
