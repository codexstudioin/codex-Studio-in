package com.example.data

import com.example.model.Category
import com.example.model.Combo
import com.example.model.CustomizationGroup
import com.example.model.CustomizationOption
import com.example.model.DiscountType
import com.example.model.FoodType
import com.example.model.Offer
import com.example.model.Product
import com.example.model.RestaurantSettings

object DefaultData {

    val defaultSettings = RestaurantSettings(
        restaurantName = "Rajput Fast Food",
        tagline = "Royal Taste, Lightning Fast!",
        phone = "+91 98765 43210",
        whatsapp = "+91 98765 43210",
        address = "Shop 12, Royal Heritage Complex, Station Road, Jaipur, Rajasthan 302001",
        openingHours = "11:00 AM – 11:30 PM (Everyday)",
        deliveryEnabled = true,
        takeawayEnabled = true,
        deliveryChargePaise = 4000L, // ₹40
        freeDeliveryThresholdPaise = 49900L, // Free delivery above ₹499
        minOrderPaise = 14900L, // ₹149 min
        aboutText = "Welcome to Rajput Fast Food! We take pride in serving high-quality, freshly made fast-food meals blending authentic Indian spices with global favorites. Every order is freshly prepared on demand for quick takeaway or doorstep delivery.",
        adminPin = "1234"
    )

    val defaultCategories = listOf(
        Category("cat_burgers", "Burgers", "Juicy patties, fresh veggies & toasted sesame buns", "", true, 1),
        Category("cat_pizza", "Pizzas", "Hand-tossed crust with 100% mozzarella cheese", "", true, 2),
        Category("cat_sandwiches", "Sandwiches", "Crispy grilled multi-layer breads & secret spices", "", true, 3),
        Category("cat_momos", "Momos", "Steamed, fried & kurkure momos with fire-hot dip", "", true, 4),
        Category("cat_fries", "Fries & Sides", "Golden crisp french fries with signature seasoning", "", true, 5),
        Category("cat_rolls", "Rolls & Wraps", "Flaky parathas packed with spicy tawa fillings", "", true, 6),
        Category("cat_pasta_noodles", "Pasta & Noodles", "Italian pasta bowls and Indo-Chinese noodles", "", true, 7),
        Category("cat_drinks", "Shakes & Drinks", "Thick cold shakes, iced coffees & coolers", "", true, 8)
    )

    private val burgerSizeGroup = CustomizationGroup(
        id = "cg_b_size",
        name = "Size & Patty",
        isMultiSelect = false,
        isRequired = true,
        options = listOf(
            CustomizationOption("opt_b_reg", "Regular Patty", 0L),
            CustomizationOption("opt_b_dbl", "Double Patty (+₹40)", 4000L)
        )
    )

    private val addCheeseGroup = CustomizationGroup(
        id = "cg_cheese",
        name = "Cheese Add-ons",
        isMultiSelect = true,
        isRequired = false,
        options = listOf(
            CustomizationOption("opt_ch_slice", "Extra Cheese Slice (+₹20)", 2000L),
            CustomizationOption("opt_ch_dip", "Cheesy Jalapeno Dip (+₹25)", 2500L)
        )
    )

    private val spiceLevelGroup = CustomizationGroup(
        id = "cg_spice",
        name = "Spice Level",
        isMultiSelect = false,
        isRequired = false,
        options = listOf(
            CustomizationOption("opt_sp_mild", "Mild Spiced", 0L),
            CustomizationOption("opt_sp_med", "Medium Hot", 0L),
            CustomizationOption("opt_sp_fire", "Extra Spicy Peri Peri (+₹10)", 1000L)
        )
    )

    private val pizzaCrustGroup = CustomizationGroup(
        id = "cg_crust",
        name = "Crust Options",
        isMultiSelect = false,
        isRequired = true,
        options = listOf(
            CustomizationOption("opt_cr_reg", "Hand Tossed Classic", 0L),
            CustomizationOption("opt_cr_burst", "Cheese Burst (+₹50)", 5000L)
        )
    )

    val defaultProducts = listOf(
        Product(
            id = "prod_001",
            name = "Rajput Maharaja Double Paneer Burger",
            categoryId = "cat_burgers",
            description = "Our signature double grilled cottage cheese patty with melted cheddar, crunchy lettuce, pickled onions & tandoori mayo in a toasted brioche bun.",
            pricePaise = 14900L,
            originalPricePaise = 17900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 15,
            ingredients = listOf("Paneer Patty", "Cheddar Cheese", "Tandoori Mayo", "Lettuce", "Toasted Bun"),
            customizationGroups = listOf(burgerSizeGroup, addCheeseGroup, spiceLevelGroup)
        ),
        Product(
            id = "prod_002",
            name = "Classic Aloo Tikki Crunchy Burger",
            categoryId = "cat_burgers",
            description = "Golden crisp spiced potato patty layered with sweet & tangy tomato relish, crisp onions and creamy mayonnaise.",
            pricePaise = 6900L,
            originalPricePaise = 8900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = false,
            displayOrder = 2,
            prepTimeMinutes = 10,
            ingredients = listOf("Crispy Potato Patty", "Herb Mayo", "Tomato Relish", "Sesame Bun"),
            customizationGroups = listOf(addCheeseGroup, spiceLevelGroup)
        ),
        Product(
            id = "prod_003",
            name = "Crispy Chicken Delight Burger",
            categoryId = "cat_burgers",
            description = "Crispy deep-fried seasoned chicken breast patty topped with spicy garlic mayo, fresh iceberg lettuce, and double cheese.",
            pricePaise = 13900L,
            originalPricePaise = 16900L,
            foodType = FoodType.NON_VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 3,
            prepTimeMinutes = 15,
            ingredients = listOf("Crispy Chicken Breast", "Garlic Mayo", "Iceberg Lettuce", "Cheese Slice"),
            customizationGroups = listOf(burgerSizeGroup, addCheeseGroup)
        ),
        Product(
            id = "prod_004",
            name = "Smoky BBQ Paneer Burger",
            categoryId = "cat_burgers",
            description = "Char-grilled paneer infused with hickory smoke BBQ sauce, caramelized onions and jalapeno peppers.",
            pricePaise = 12900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 4,
            prepTimeMinutes = 12,
            customizationGroups = listOf(addCheeseGroup, spiceLevelGroup)
        ),
        Product(
            id = "prod_005",
            name = "Rajput Special Farmhouse Pizza (8\")",
            categoryId = "cat_pizza",
            description = "Loaded with fresh crunchy capsicum, sweet golden corn, red onions, black olives, ripe tomatoes and mozzarella on rich herbed marinara sauce.",
            pricePaise = 23900L,
            originalPricePaise = 27900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 20,
            ingredients = listOf("Fresh Dough", "Mozzarella", "Golden Corn", "Bell Peppers", "Olives", "Herb Marinara"),
            customizationGroups = listOf(pizzaCrustGroup, addCheeseGroup)
        ),
        Product(
            id = "prod_006",
            name = "Margherita Double Mozzarella Pizza (8\")",
            categoryId = "cat_pizza",
            description = "Classic Italian delight featuring our aromatic tomato sauce, basil, and a generous layer of melted mozzarella cheese.",
            pricePaise = 16900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 15,
            customizationGroups = listOf(pizzaCrustGroup, addCheeseGroup)
        ),
        Product(
            id = "prod_007",
            name = "Spicy Chicken Tikka Pizza (8\")",
            categoryId = "cat_pizza",
            description = "Smoky tandoori chicken tikka cubes, red paprika, sliced onions and mozzarella topped with fresh coriander drizzle.",
            pricePaise = 26900L,
            originalPricePaise = 30900L,
            foodType = FoodType.NON_VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 3,
            prepTimeMinutes = 20,
            ingredients = listOf("Tandoori Chicken Tikka", "Mozzarella", "Red Paprika", "Onions"),
            customizationGroups = listOf(pizzaCrustGroup)
        ),
        Product(
            id = "prod_008",
            name = "Rajput Bombay Club Triple Decker Sandwich",
            categoryId = "cat_sandwiches",
            description = "Three toasted buttered bread layers with spiced potato beetroot masala, cucumber, tomato, mint chutney and melted cheese.",
            pricePaise = 12900L,
            originalPricePaise = 14900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 12,
            customizationGroups = listOf(addCheeseGroup, spiceLevelGroup)
        ),
        Product(
            id = "prod_009",
            name = "Grilled Corn & Cheddar Cheese Sandwich",
            categoryId = "cat_sandwiches",
            description = "Crispy golden butter-toasted sandwich filled with sweet corn kernels, melted cheddar cheese and Italian herbs.",
            pricePaise = 8900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 10,
            customizationGroups = listOf(addCheeseGroup)
        ),
        Product(
            id = "prod_010",
            name = "Kurkure Fried Paneer Momos (8 Pcs)",
            categoryId = "cat_momos",
            description = "Crunchy crispy coating on juicy cottage cheese dumplings, served piping hot with fiery garlic chilli sauce and mayo dip.",
            pricePaise = 11900L,
            originalPricePaise = 13900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 12,
            ingredients = listOf("Paneer", "Crunchy Kurkure Crust", "Fiery Red Chutney", "Mayonnaise"),
            customizationGroups = listOf(addCheeseGroup)
        ),
        Product(
            id = "prod_011",
            name = "Steamed Darjeeling Veg Momos (8 Pcs)",
            categoryId = "cat_momos",
            description = "Traditional thin-wrapper steamed dumplings filled with finely chopped cabbage, carrots, spring onions and ginger garlic.",
            pricePaise = 8900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 10,
            ingredients = listOf("Fresh Vegetables", "Chilli Garlic Dip"),
            customizationGroups = listOf()
        ),
        Product(
            id = "prod_012",
            name = "Crispy Fried Chicken Momos (8 Pcs)",
            categoryId = "cat_momos",
            description = "Succulent minced chicken wrapped and fried to a deep golden crunch, paired with spicy Himalayan momo dip.",
            pricePaise = 12900L,
            originalPricePaise = 14900L,
            foodType = FoodType.NON_VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 3,
            prepTimeMinutes = 12,
            customizationGroups = listOf(addCheeseGroup)
        ),
        Product(
            id = "prod_013",
            name = "Peri Peri Masala French Fries",
            categoryId = "cat_fries",
            description = "Extra crispy golden potatoes tossed in our zesty African peri peri seasoning. Served with creamy herb dip.",
            pricePaise = 8900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 8,
            customizationGroups = listOf(addCheeseGroup)
        ),
        Product(
            id = "prod_014",
            name = "Cheesy Lava Loaded Fries",
            categoryId = "cat_fries",
            description = "Crispy fries smothered in hot melted cheddar sauce, mozzarella cheese, chopped jalapenos and chili flakes.",
            pricePaise = 11900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 10,
            customizationGroups = listOf()
        ),
        Product(
            id = "prod_015",
            name = "Royal Paneer Tikka Kathi Roll",
            categoryId = "cat_rolls",
            description = "Marinated paneer cubes roasted on skewers, rolled in a flaky layered paratha with mint relish, sliced onions and chaat masala.",
            pricePaise = 9900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 12,
            customizationGroups = listOf(addCheeseGroup, spiceLevelGroup)
        ),
        Product(
            id = "prod_016",
            name = "Butter Chicken Kathi Roll",
            categoryId = "cat_rolls",
            description = "Tender pulled chicken tossed in rich makhani gravy wrapped inside an egg-coated crispy paratha.",
            pricePaise = 12900L,
            foodType = FoodType.NON_VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 15,
            customizationGroups = listOf(addCheeseGroup)
        ),
        Product(
            id = "prod_017",
            name = "Creamy White Sauce Penne Alfredo",
            categoryId = "cat_pasta_noodles",
            description = "Al dente penne pasta bathed in rich velvety parmesan cream sauce with sauteed mushrooms, corn and bell peppers.",
            pricePaise = 13900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 1,
            prepTimeMinutes = 15,
            customizationGroups = listOf(addCheeseGroup)
        ),
        Product(
            id = "prod_018",
            name = "Schezwan Veg Hakka Noodles",
            categoryId = "cat_pasta_noodles",
            description = "Wok-tossed noodles with shredded cabbage, capsicum, carrots and fiery authentic Schezwan sauce.",
            pricePaise = 11900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 12,
            customizationGroups = listOf()
        ),
        Product(
            id = "prod_019",
            name = "Royal Belgian Chocolate Shake",
            categoryId = "cat_drinks",
            description = "Ultra thick creamy milkshake made with real Belgian dark chocolate, topped with whipped cream and chocolate fudge drizzle.",
            pricePaise = 9900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            isBestseller = true,
            displayOrder = 1,
            prepTimeMinutes = 6,
            customizationGroups = listOf()
        ),
        Product(
            id = "prod_020",
            name = "Cold Coffee with Vanilla Ice Cream",
            categoryId = "cat_drinks",
            description = "Smooth blended espresso coffee poured over chilled milk, capped with a scoop of rich vanilla ice cream and cocoa dust.",
            pricePaise = 8900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 2,
            prepTimeMinutes = 5,
            customizationGroups = listOf()
        ),
        Product(
            id = "prod_021",
            name = "Fresh Masala Lime Soda",
            categoryId = "cat_drinks",
            description = "Sparkling chilled soda with freshly squeezed lime juice, roasted cumin, black salt and mint leaves.",
            pricePaise = 4900L,
            foodType = FoodType.VEG,
            isAvailable = true,
            displayOrder = 3,
            prepTimeMinutes = 4,
            customizationGroups = listOf()
        )
    )

    val defaultOffers = listOf(
        Offer(
            id = "offer_01",
            code = "RAJPUT50",
            title = "₹50 Flat OFF",
            description = "Flat ₹50 off on orders above ₹299. Valid on all burgers, pizzas & rolls.",
            discountType = DiscountType.FLAT,
            discountValue = 5000L, // ₹50
            minOrderPaise = 29900L,
            maxDiscountPaise = 5000L,
            validUntil = "31 Dec",
            isEnabled = true
        ),
        Offer(
            id = "offer_02",
            code = "ROYAL20",
            title = "20% Royal Discount",
            description = "Get 20% OFF up to ₹100 on orders above ₹399.",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20L,
            minOrderPaise = 39900L,
            maxDiscountPaise = 10000L, // ₹100 max
            validUntil = "Ongoing",
            isEnabled = true
        ),
        Offer(
            id = "offer_03",
            code = "FREEDEL",
            title = "Free Doorstep Delivery",
            description = "Waive delivery fee on orders above ₹249.",
            discountType = DiscountType.FLAT,
            discountValue = 4000L, // ₹40 delivery fee
            minOrderPaise = 24900L,
            maxDiscountPaise = 4000L,
            validUntil = "Limited Time",
            isEnabled = true
        )
    )

    val defaultCombos = listOf(
        Combo(
            id = "combo_01",
            name = "Maharaja Royal Meal Box",
            description = "1x Rajput Maharaja Burger + 1x Peri Peri Fries + 1x Cold Coffee with Ice Cream. Royal feast for one!",
            pricePaise = 24900L,
            originalPricePaise = 32700L,
            includedItemIds = listOf("prod_001", "prod_013", "prod_020"),
            isAvailable = true
        ),
        Combo(
            id = "combo_02",
            name = "Pizza & Loaded Fries Duo Feast",
            description = "1x Farmhouse Special Pizza (8\") + 1x Cheesy Lava Loaded Fries + 2x Masala Lime Sodas. Perfect party meal!",
            pricePaise = 36900L,
            originalPricePaise = 45600L,
            includedItemIds = listOf("prod_005", "prod_014", "prod_021"),
            isAvailable = true
        ),
        Combo(
            id = "combo_03",
            name = "Momos & Kathi Roll Craving Box",
            description = "1x Kurkure Paneer Momos (8 Pcs) + 1x Paneer Tikka Kathi Roll + 1x Fresh Masala Soda.",
            pricePaise = 21900L,
            originalPricePaise = 26700L,
            includedItemIds = listOf("prod_010", "prod_015", "prod_021"),
            isAvailable = true
        )
    )
}
