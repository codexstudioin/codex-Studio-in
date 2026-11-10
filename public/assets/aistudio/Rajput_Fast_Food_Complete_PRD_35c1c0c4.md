# RAJPUT FAST FOOD

## Complete Product Requirements Document (PRD)

### Android Customer App + Separate Android Admin App

**Document status:** Final product-level specification\
**Product category:** Food & Beverage / Fast Food Ordering\
**Platform:** Android only\
**Brand theme:** Red, black and white\
**Architecture:** Frontend-only, offline-first, local device storage\
**Backend:** None\
**Database:** None\
**Custom APIs:** None

------------------------------------------------------------------------

# 1. Executive Summary

Rajput Fast Food is a two-application Android product for a fast-food
restaurant.

1.  **Customer App:** Lets customers browse the restaurant's food menu,
    search for items, view product details, manage favorites and a
    shopping cart, choose delivery or takeaway, calculate an order
    total, and hand off the order through WhatsApp or a phone call.
2.  **Admin App:** A separate Android application for locally managing
    the menu, categories, product prices, availability, offers,
    restaurant details, and local configuration.

Both applications must be developed without a backend, database, or
custom APIs. Each app stores its own data locally. The admin app and
customer app do not automatically synchronize. Menu updates must be
distributed manually, for example by exporting a JSON catalogue from the
admin app and importing it into a customer app installation, or by
publishing a new app build with updated bundled data.

The product must not claim to provide real-time order tracking, online
payment processing, automatic order reception, cloud synchronization, or
multi-device data sharing.

# 2. Confirmed Project Configuration

  Requirement                    Decision
  ------------------------------ -----------------------------------------
  Brand name                     Rajput Fast Food
  Product type                   Online fast-food ordering experience
  Platform                       Android only
  Customer app                   Separate Android application
  Admin app                      Separate Android application
  Ordering methods               WhatsApp and phone call
  Fulfilment methods             Delivery and takeaway
  Visual theme                   Red, black and white
  Backend                        Not included
  Database                       Not included
  Custom APIs                    Not included
  Data storage                   Local device storage and bundled assets
  Initial menu data              Local JSON/assets
  Cross-device synchronization   Not included
  Online payment gateway         Not included
  Real-time order tracking       Not included

# 3. Product Vision and Goals

## 3.1 Vision

Create a polished, easy-to-use Android food ordering app that presents
Rajput Fast Food's menu professionally and makes it simple for customers
to contact the restaurant and place an order.

## 3.2 Goals

-   Present food items with clear names, descriptions, images and
    prices.
-   Make menu browsing and search simple.
-   Let customers add, remove and customize items where supported.
-   Calculate cart totals accurately.
-   Support both delivery and takeaway order preparation.
-   Provide WhatsApp and phone-call order handoff.
-   Allow the restaurant operator to manage catalogue data locally in a
    separate admin app.
-   Provide a consistent red, black and white brand identity.
-   Work without a custom backend, database or custom API.

## 3.3 Non-goals

The following are explicitly outside the current scope:

-   Customer accounts synchronized across devices.
-   Cloud-hosted menu management.
-   Automatic synchronization between admin and customer apps.
-   Restaurant-side automatic order inbox.
-   Real-time order status or delivery tracking.
-   Online payment gateway or automatic UPI verification.
-   Delivery-partner app or rider tracking.
-   Multi-branch management.
-   Server-based analytics.
-   Push notifications from a server.

# 4. Target Users and Roles

## 4.1 Customer

A person who wants to browse the restaurant menu and contact Rajput Fast
Food to place a delivery or takeaway order.

## 4.2 Administrator

The restaurant owner or authorized operator who uses the separate admin
app to manage local menu and business information.

## 4.3 Important security limitation

Because there is no backend, local admin authentication is only a
convenience barrier. It is not strong security against a person with
device access, app inspection tools or access to local files. Do not
describe it as production-grade authentication.

# 5. Application Architecture

The solution consists of two independent Android applications:

## 5.1 Customer App

Responsibilities: - Display bundled menu data. - Display locally
imported menu updates if import is implemented. - Search and filter food
items. - Manage cart and favorites. - Collect order details. - Calculate
order totals. - Generate WhatsApp order messages. - Launch the phone
dialler. - Save optional local order history and preferences.

## 5.2 Admin App

Responsibilities: - Provide local admin access screen. - Manage products
and categories stored on that device. - Manage prices, availability,
descriptions and offers. - Manage restaurant contact and business
settings. - Select local images. - Export and import menu data. -
Display local reports based only on data available on that device.

## 5.3 Data isolation

The apps do not share a live data store. An edit made in the admin app
must not be presented as if it has automatically changed the customer
app.

Recommended manual update workflow: 1. Admin edits the menu in the Admin
App. 2. Admin exports a versioned JSON catalogue file. 3. The file is
transferred to the customer device by a manual method. 4. The Customer
App imports and validates the file, if import support is implemented. 5.
The customer app displays the imported catalogue. 6. If manual import is
not implemented, menu changes require an updated app build.

# 6. Customer App -- Sitemap and Screen Inventory

1.  Splash Screen
2.  Onboarding Screen (optional and skippable)
3.  Welcome / Home Screen
4.  Food Categories Screen
5.  Category Products Screen
6.  All Menu Screen
7.  Product Details Screen
8.  Search Screen
9.  Search Results Screen
10. Offers Screen
11. Combo Details Screen
12. Favorites Screen
13. Cart Screen
14. Checkout Screen
15. Order Summary Screen
16. Order Method Selection Screen
17. WhatsApp Handoff Screen
18. Phone Call Handoff Screen
19. Local Order History Screen (optional)
20. Local Order Details Screen (optional)
21. Customer Profile / Preferences Screen (optional; no account service)
22. Saved Addresses Screen (optional)
23. About Us Screen
24. Contact Us Screen
25. Help & Support Screen
26. FAQ Screen
27. Settings Screen
28. Privacy Policy Screen
29. Terms and Conditions Screen
30. Empty, loading, error and unavailable states

## 6.1 Navigation

Recommended bottom navigation: - Home - Menu - Favorites - Cart -
Profile / More

The cart icon should show the current item count. Navigation labels and
icons must remain consistent throughout the app.

# 7. Customer App -- Functional Requirements

## 7.1 Splash Screen

-   Display the Rajput Fast Food logo.
-   Use the brand colors.
-   Provide a short, smooth animation.
-   Navigate to Home or onboarding.
-   Avoid unnecessary long loading delays.

## 7.2 Home Screen

Required sections: - Brand header and logo. - Delivery / takeaway
selection or clear ordering entry point. - Search field. - Promotional
banner area. - Food category list. - Best sellers section, only when
items are marked as best sellers. - Popular items section. - Special
combos. - Offers. - Restaurant opening hours. - Contact information. -
Bottom navigation.

Home content must be sourced from bundled or locally imported data. Do
not invent ratings, popularity statistics or customer reviews.

## 7.3 Categories

Suggested starter categories, subject to confirmation against the real
restaurant menu: - Burgers - Pizza - Sandwiches - Momos - French Fries -
Pasta - Noodles - Chinese Food - Rolls - Garlic Bread - Snacks - Cold
Drinks - Shakes - Combos

Requirements: - Show category image, name and optional description. -
Open the selected category. - Handle categories with no available
items. - Support category ordering where data defines it.

## 7.4 Product Catalogue

Each product may contain: - Unique product ID. - Product name. -
Category ID. - Description. - Image asset or local image reference. -
Current price in INR. - Optional original price. - Optional discount
label. - Vegetarian / non-vegetarian indicator. - Availability status. -
Optional preparation-time estimate. - Optional bestseller flag. -
Optional ingredients and allergen information. - Optional customization
groups. - Display order.

Requirements: - Display clear images and prices. - Hide or disable
ordering for unavailable items. - Do not display unsupported claims. -
Handle missing images with a fallback image. - Keep product cards
visually consistent.

## 7.5 Product Details

Display: - Large product image. - Name and description. - Price. - Veg /
non-veg indicator. - Ingredients when provided. - Customization options
when supported. - Quantity selector. - Special instructions field, if
supported. - Add to Cart button. - Similar items where suitable data
exists.

Customization examples, only if offered by the restaurant: - Size. -
Spice level. - Extra cheese. - Extra toppings. - Sauces. - Ingredient
removal.

The app must not offer a customization that the restaurant cannot
fulfil.

## 7.6 Search

-   Search product names.
-   Search category names where useful.
-   Ignore leading/trailing spaces.
-   Handle case differences.
-   Provide clear-search action.
-   Show helpful empty results.
-   Search only local catalogue data.

## 7.7 Favorites

-   Add or remove favorite products.
-   Persist favorites locally where practical.
-   Handle deleted or unavailable products.
-   Show an empty-state message when no favorites exist.

## 7.8 Offers and Combos

-   Display locally configured offers.
-   Show validity dates only when supplied.
-   Clearly state conditions and exclusions.
-   Support enabled/disabled status.
-   Do not imply that an expired offer is valid.
-   Do not invent discount values.

## 7.9 Cart

Required features: - Add item. - Remove item. - Increase quantity. -
Decrease quantity. - Display selected customizations. - Edit item
instructions if supported. - Clear cart with confirmation. - Continue
shopping. - Proceed to checkout. - Persist cart locally if appropriate.

Cart rules: - Recalculate totals whenever quantity, item or
customization changes. - Validate item availability before preparing an
order. - Prevent invalid quantities. - Use integer paise internally for
currency arithmetic. - Display currency as INR (₹).

Calculation: - Subtotal = sum of each item price multiplied by
quantity. - Discount = valid locally configured discount. - Delivery
charge = configured charge for delivery, if applicable. - Grand total =
subtotal + applicable charges - discount.

Do not apply delivery charges to takeaway unless the restaurant
explicitly configures them.

## 7.10 Checkout

Collect only information needed for the selected fulfilment method.

Common fields: - Customer name. - Contact phone number. - Order type:
delivery or takeaway. - Optional special instructions.

Delivery fields: - Full address. - Landmark, if useful. - Delivery
instructions.

Takeaway fields: - Optional preferred pickup time. - Optional pickup
instructions.

Validation: - Name cannot be blank. - Phone number must pass a
reasonable format check for the configured country. - Delivery address
is required for delivery. - Cart cannot be empty. - Unavailable items
cannot be ordered. - Show clear field-level validation messages.

## 7.11 Order Summary

Show: - Restaurant name. - Customer details. - Order type. - Address for
delivery. - Item names. - Quantities. - Customizations. - Item totals. -
Subtotal. - Discount. - Delivery charge, if applicable. - Grand total. -
Selected order method.

The user must be able to return and edit the cart before handoff.

## 7.12 WhatsApp Order Handoff

-   Use the configured restaurant WhatsApp number.
-   Generate a readable, URL-encoded order message.
-   Include item names, quantities, customizations, order type, customer
    details and calculated total.
-   Include delivery address only for delivery orders.
-   Open WhatsApp using an Android-compatible external intent or link.
-   Handle the case where WhatsApp is unavailable.
-   Offer the phone-call alternative.

The app must clearly explain that the customer still needs to press Send
in WhatsApp. Opening WhatsApp does not mean the order has been received
or confirmed.

## 7.13 Phone Call Handoff

-   Use the configured restaurant phone number.
-   Open the Android dialler with the number.
-   Prefer the dialler confirmation flow rather than placing a call
    silently.
-   If the phone app cannot be opened, display the number so it can be
    copied.
-   Do not claim the call was completed or the order was confirmed.

## 7.14 Order History

Optional local-only feature: - Save a local copy of an order summary
when the user initiates handoff. - Label it clearly as "Prepared" or
"Sent to WhatsApp" only when the app can verify the corresponding
action. - Never label an order "Confirmed" or "Preparing" without
restaurant confirmation. - Allow local deletion of history. - Explain
that local history may be lost if app data is cleared or the app is
uninstalled.

## 7.15 Profile and Addresses

These are local convenience features, not online accounts: - Save
customer name and phone locally if the user chooses. - Save multiple
addresses locally if implemented. - Edit and delete saved details. -
Provide a clear option to erase local data. - Do not request unnecessary
sensitive information.

## 7.16 Contact and Information

Include configurable: - Restaurant address. - Phone number. - WhatsApp
number. - Opening hours. - About text. - FAQ. - Privacy policy. - Terms
and conditions.

Use placeholders until the owner provides verified information.

# 8. Admin App -- Sitemap and Screen Inventory

1.  Admin Splash Screen
2.  Admin Login Screen
3.  Admin Dashboard
4.  Product Management
5.  Add Product
6.  Edit Product
7.  Product Preview
8.  Category Management
9.  Add Category
10. Edit Category
11. Offers Management
12. Add / Edit Offer
13. Combo Management
14. Image Selection / Management
15. Local Order Records (limited)
16. Local Reports
17. Restaurant Information
18. Contact Settings
19. Delivery Settings
20. Opening Hours
21. App Branding / Appearance
22. Admin Profile
23. Admin Settings
24. Export Menu
25. Import Menu
26. Backup / Restore
27. Confirmation and error dialogs

# 9. Admin App -- Functional Requirements

## 9.1 Local Admin Login

-   Provide a login screen.
-   Use a locally configured PIN or password only as a convenience
    barrier.
-   Do not store a plain-text password where avoidable.
-   Explain that this is not secure server authentication.
-   Provide a logout action.
-   Do not include a hardcoded universal secret in a production build.

## 9.2 Dashboard

Display local information only: - Total products. - Available
products. - Unavailable products. - Total categories. - Active offers. -
Recently edited products. - Quick actions.

Do not label these local catalogue counts as sales or live business
analytics.

## 9.3 Product Management

Admin can: - Add products. - Edit products. - Delete products with
confirmation. - Change product name. - Edit description. - Change
category. - Set price. - Set optional original price. - Set veg/non-veg
indicator. - Set availability. - Mark bestseller. - Select local
image. - Configure supported customizations. - Set display order.

Validation: - Product name is required. - Category must exist. - Price
must be a valid non-negative amount. - Product ID must be unique. -
Image references must be valid or use a fallback. - Invalid product data
must not corrupt the catalogue.

## 9.4 Category Management

Admin can: - Add category. - Edit category. - Delete category with
confirmation. - Select category image. - Change category display
order. - Enable or disable category.

If a category contains products, require the admin to reassign those
products or confirm a safe deletion strategy.

## 9.5 Offers Management

Admin can: - Create an offer. - Edit an offer. - Enable or disable an
offer. - Set discount type and value. - Set optional validity dates. -
Set minimum order conditions. - Add terms and exclusions.

Validate discount values and dates. The app must not silently apply
expired or disabled offers.

## 9.6 Combo Management

-   Create combo products.
-   Select included items.
-   Set combo price.
-   Add description and image.
-   Set availability.
-   Validate that included products exist.

## 9.7 Image Management

-   Select images from the device using Android's system picker.
-   Preview selected image.
-   Validate supported image formats.
-   Provide fallback images.
-   Avoid requesting broad storage access when a system picker is
    sufficient.
-   Warn before deleting an image referenced by a product.

## 9.8 Restaurant Settings

Configurable local values: - Restaurant name. - Logo. - Address. - Phone
number. - WhatsApp number. - Opening hours. - Delivery availability. -
Takeaway availability. - Delivery charge. - Minimum order value. -
Restaurant description. - Currency display. - Theme settings.

## 9.9 Local Reports and Order Records

Because no backend exists: - Reports can only use records stored or
imported on the admin device. - The admin app cannot automatically
receive customer orders. - WhatsApp conversations and phone calls are
not automatically imported. - Any manually entered order record must be
identified as manually recorded. - Do not present estimates as verified
sales.

## 9.10 Export and Import

Recommended approach: - Export a versioned JSON menu file. - Include
categories, products, offers, combos and supported settings. - Validate
the file before import. - Check schema version. - Reject malformed
data. - Preview changes before replacing current data. - Keep a local
backup before import. - Provide clear success and failure messages.

Important: Product images need special handling. JSON references to an
image path on another phone will not work. Either package images into an
export archive with relative paths or require the user to reselect
images after import. The implementation must not assume that device file
paths are portable.

# 10. Visual Design System

## 10.1 Brand colors

  Token          Color            HEX
  -------------- ---------------- ---------
  Primary        Deep Red         #B91C1C
  Primary Dark   Dark Red         #7F1D1D
  Secondary      Charcoal Black   #171717
  Background     White            #FFFFFF
  Surface        Light Grey       #F5F5F5
  Accent         Warm Red         #DC2626
  Success        Green            #16A34A
  Warning        Amber            #D97706
  Error          Red              #DC2626

## 10.2 Typography

-   Poppins Bold: main headings.
-   Poppins SemiBold: section headings and buttons.
-   Inter Regular: body text.
-   Inter Medium: labels.
-   Inter Bold: prices and important values.

Fonts must be bundled or loaded from app assets; the app must not depend
on a remote font service.

## 10.3 UI principles

-   Modern, clean and premium food-store appearance.
-   Strong food photography.
-   Consistent spacing.
-   Clear hierarchy.
-   High-contrast text.
-   Touch targets suitable for mobile use.
-   Consistent cards and buttons.
-   Minimal visual clutter.
-   Accessible labels and focus behavior.
-   Support different Android screen sizes and font scaling.

## 10.4 Components

Reusable components: - App header. - Search bar. - Category tile. - Food
card. - Price label. - Veg/non-veg indicator. - Offer banner. - Quantity
selector. - Primary and secondary buttons. - Cart summary. - Empty
state. - Error state. - Confirmation dialog. - Toast / snackbar. -
Bottom navigation.

## 10.5 Animation

-   Logo animation on splash.
-   Smooth navigation transitions.
-   Add-to-cart feedback.
-   Cart count animation.
-   Banner carousel.
-   Button press feedback.
-   Loading indicators where actual loading occurs.

Animations must respect reduced-motion preferences where practical and
must not reduce performance.

# 11. Technical Architecture

## 11.1 Recommended technology

Recommended framework: Flutter.

-   Language: Dart.
-   UI: Flutter Material 3.
-   Platform: Android.
-   State management: Provider, Riverpod or simple built-in state
    management selected by the developer.
-   Initial content: bundled JSON.
-   Images: bundled assets.
-   Small preferences: SharedPreferences or equivalent.
-   Larger editable local files: application-private file storage.
-   Image selection: Android system picker.
-   External actions: Android intents / URL launcher.

No cloud SDK, backend SDK, database package or custom API client should
be added.

## 11.2 Suggested repository structure

``` text
RAJPUT_FAST_FOOD/
├── customer_app/
│   ├── assets/
│   │   ├── images/
│   │   │   ├── logo/
│   │   │   ├── banners/
│   │   │   ├── food/
│   │   │   └── categories/
│   │   ├── fonts/
│   │   └── data/
│   │       ├── products.json
│   │       ├── categories.json
│   │       └── offers.json
│   ├── lib/
│   │   ├── core/
│   │   ├── models/
│   │   ├── data/
│   │   ├── services/
│   │   ├── widgets/
│   │   └── screens/
│   ├── test/
│   └── pubspec.yaml
│
├── admin_app/
│   ├── assets/
│   ├── lib/
│   │   ├── core/
│   │   ├── models/
│   │   ├── services/
│   │   ├── widgets/
│   │   └── screens/
│   ├── test/
│   └── pubspec.yaml
│
└── documentation/
    ├── CUSTOMER_APP_PRD.md
    ├── ADMIN_APP_PRD.md
    ├── DESIGN_SYSTEM.md
    ├── DATA_FORMAT.md
    ├── USER_FLOWS.md
    └── TEST_PLAN.md
```

# 12. Local Data Model (Not a Database)

Use typed in-memory models and local JSON files. Do not create a
database.

## 12.1 Product example

``` json
{
  "id": "food_001",
  "name": "Classic Burger",
  "categoryId": "cat_burgers",
  "description": "Fresh and delicious classic burger",
  "pricePaise": 9900,
  "imageAsset": "assets/images/food/burger.png",
  "foodType": "veg",
  "isAvailable": true,
  "isBestseller": true,
  "displayOrder": 1,
  "customizationGroupIds": []
}
```

This is sample data only. Actual menu items, prices and descriptions
must be provided by the restaurant.

## 12.2 Category example

``` json
{
  "id": "cat_burgers",
  "name": "Burgers",
  "imageAsset": "assets/images/categories/burgers.png",
  "isEnabled": true,
  "displayOrder": 1
}
```

## 12.3 Offer example

``` json
{
  "id": "offer_001",
  "title": "Special Offer",
  "description": "Offer description",
  "discountType": "percentage",
  "discountValue": 10,
  "isEnabled": false,
  "validFrom": null,
  "validUntil": null
}
```

## 12.4 Storage rules

-   Keep bundled default data read-only.
-   Store admin edits in app-private files.
-   Store simple preferences separately.
-   Validate all imported data.
-   Keep backup copies before destructive changes.
-   Handle app data clearing and uninstall limitations honestly.
-   Do not store payment credentials.
-   Do not store unnecessary sensitive customer information.

# 13. Order Flow

## 13.1 Delivery flow

1.  Customer opens app.
2.  Customer browses menu.
3.  Customer selects products.
4.  Customer adds products to cart.
5.  Customer selects Delivery.
6.  Customer enters name, phone and delivery address.
7.  App validates fields.
8.  App calculates order total.
9.  Customer reviews order summary.
10. Customer chooses WhatsApp or Phone.
11. App opens WhatsApp with prepared message or opens the dialler.
12. Customer sends the WhatsApp message or calls the restaurant.
13. Restaurant staff manually reviews and confirms the order.

## 13.2 Takeaway flow

1.  Customer browses menu.
2.  Customer adds products to cart.
3.  Customer selects Takeaway.
4.  Customer enters name and phone.
5.  Customer optionally selects a requested pickup time.
6.  App calculates order total without delivery charge.
7.  Customer reviews order.
8.  Customer chooses WhatsApp or Phone.
9.  Restaurant staff manually confirms availability and pickup details.

# 14. Order Message Format

Example message generated for WhatsApp:

``` text
Hello Rajput Fast Food!

I would like to place an order.

Customer: [Customer Name]
Phone: [Customer Phone]
Order Type: [Delivery / Takeaway]

ITEMS
1. Classic Burger x 2 - ₹198
2. French Fries x 1 - ₹79

Subtotal: ₹277
Delivery Charge: ₹[Configured Charge]
Discount: ₹[Discount]
Grand Total: ₹[Final Total]

Delivery Address:
[Address, if delivery]

Instructions:
[Customer Instructions]

Please confirm my order.
Thank you!
```

All bracketed values must be replaced with validated customer and cart
data. Do not use placeholder text in a real order handoff.

# 15. Security, Privacy and Permissions

-   Validate all user inputs.
-   Avoid unnecessary permissions.
-   Use Android's system file/image picker.
-   Keep local files in app-private storage where possible.
-   Do not store payment credentials.
-   Do not include secrets in source code.
-   Use a local admin lock only as a convenience barrier.
-   Add confirmation for deletion and reset actions.
-   Provide a local data-clear option.
-   Explain that uninstalling or clearing app data may erase local
    records.
-   Provide accurate privacy disclosures.
-   Do not claim that local-only storage is cloud-backed or
    synchronized.

# 16. Error and Empty States

Implement clear states for: - Empty cart. - Empty favorites. - No search
results. - Empty category. - Product unavailable. - Missing product
image. - Invalid phone number. - Missing delivery address. - Invalid
coupon. - Invalid imported JSON. - Unsupported import schema. - Failed
image selection. - WhatsApp unavailable. - Phone dialler unavailable. -
Local file read/write failure. - Admin login failure. - Failed backup
restore.

Messages must explain what happened and what the user can do next.

# 17. Non-functional Requirements

## Performance

-   Optimize and compress food images.
-   Load images efficiently.
-   Avoid unnecessary rebuilds.
-   Keep animations lightweight.
-   Minimize startup time.
-   Test on low-memory Android devices.

## Accessibility

-   Maintain readable contrast.
-   Support Android font scaling.
-   Provide semantic labels for controls.
-   Use adequate touch target sizes.
-   Do not communicate availability or food type using color alone.

## Reliability

-   Validate data before saving.
-   Protect against corrupted imports.
-   Preserve the previous catalogue if an import fails.
-   Handle missing local files gracefully.
-   Do not crash if an optional setting is missing.

## Compatibility

-   Android phones of different screen sizes.
-   Portrait layout as the primary orientation.
-   Current supported Android versions to be selected during
    implementation.
-   Test on both low-end and modern Android devices.

# 18. Testing Plan

## Customer App

-   Splash and navigation.
-   Home screen rendering.
-   Category browsing.
-   Product details.
-   Search.
-   Favorites persistence.
-   Cart add/remove/update.
-   Quantity validation.
-   Price calculations.
-   Delivery charge rules.
-   Coupon rules.
-   Delivery checkout validation.
-   Takeaway checkout validation.
-   WhatsApp message generation and encoding.
-   Phone dialler handoff.
-   Empty and error states.
-   Local data persistence.

## Admin App

-   Local login flow.
-   Dashboard calculations.
-   Add/edit/delete product.
-   Product validation.
-   Category management.
-   Offer validation.
-   Combo management.
-   Image picker.
-   Restaurant settings.
-   JSON export.
-   JSON import.
-   Invalid file handling.
-   Backup and restore.
-   Data reset confirmation.

## Cross-app/manual testing

-   Export menu from Admin App.
-   Transfer file manually.
-   Import into Customer App.
-   Confirm that imported menu appears.
-   Confirm that no automatic synchronization is claimed.
-   Confirm that customer order handoff does not create an admin-side
    order automatically.

# 19. Development Roadmap

## Phase 1 -- Planning

-   Confirm real restaurant details.
-   Confirm menu and categories.
-   Confirm framework and Android minimum version.
-   Confirm app names and icons.

## Phase 2 -- Branding and Design

-   Create logo and icon.
-   Finalize colors and typography.
-   Create reusable components.
-   Design screen layouts.

## Phase 3 -- Customer App

-   Build navigation.
-   Build home and catalogue.
-   Build categories and search.
-   Build product details.
-   Build favorites and cart.
-   Build checkout.
-   Build WhatsApp and phone handoff.
-   Add local persistence.

## Phase 4 -- Admin App

-   Build local login.
-   Build dashboard.
-   Build product and category management.
-   Build offers and combos.
-   Build image selection.
-   Build settings.
-   Build import/export and backup.

## Phase 5 -- Quality Assurance

-   Functional testing.
-   UI testing.
-   Device compatibility testing.
-   Performance testing.
-   Import/export testing.
-   Order handoff testing.

## Phase 6 -- Release Preparation

-   Create app icons.
-   Configure Android application IDs.
-   Generate signed release builds.
-   Test APK installation.
-   Prepare store listing assets if publishing.
-   Prepare privacy policy and required disclosures.

# 20. Acceptance Criteria

The project is complete when:

## Customer App

-   All required screens are implemented.
-   Menu and category data display correctly.
-   Search and product details work.
-   Favorites and cart work.
-   All totals are accurate.
-   Delivery and takeaway flows work.
-   WhatsApp message is generated correctly.
-   Phone dialler handoff works.
-   Local preferences persist as specified.
-   No false order confirmation is shown.

## Admin App

-   It is a separate Android application.
-   Local login interface works.
-   Products can be added, edited and deleted.
-   Categories can be managed.
-   Offers and settings can be managed.
-   Local image selection works.
-   JSON export/import works if included in the release scope.
-   Invalid imports do not destroy existing data.
-   Reports are limited to locally available information.

## Both Apps

-   Android-only builds are produced.
-   Red, black and white branding is consistent.
-   UI works across supported device sizes.
-   No backend is used.
-   No database is used.
-   No custom APIs are used.
-   No automatic synchronization is claimed.
-   Source code and build instructions are delivered.

# 21. Final Deliverables

1.  Complete Customer Android app source code.
2.  Complete Admin Android app source code.
3.  Customer APK or release-ready Android build.
4.  Admin APK or release-ready Android build.
5.  All local image and branding assets.
6.  Bundled JSON sample catalogue.
7.  Local storage implementation.
8.  WhatsApp and phone handoff.
9.  Admin menu management.
10. JSON import/export and backup.
11. Test plan and test results.
12. Build and installation instructions.
13. Privacy and permission documentation.
14. Final project source folder.

# 22. Information Required from the Restaurant Owner

The following details are still pending and must be supplied before
production release:

-   Verified restaurant address.
-   Restaurant phone number.
-   WhatsApp number with country code.
-   Actual menu items.
-   Actual prices.
-   Product descriptions and food images.
-   Confirmed food categories.
-   Delivery charges.
-   Minimum order amount, if any.
-   Delivery service area.
-   Opening and closing hours.
-   Holiday schedule.
-   Actual logo and brand assets.
-   Confirmed ordering and cancellation policies.

Until these are supplied, use clearly marked placeholders and sample
products. Do not invent real business details.

# 23. Instructions to the AI Development Agent

Act as a senior Flutter Android developer, product designer and QA
engineer.

Build two separate Android applications for Rajput Fast Food according
to this PRD.

Mandatory constraints: 1. Android only. 2. Flutter and Dart are the
recommended implementation. 3. Separate Customer App and Admin App. 4.
Red, black and white design system. 5. No backend. 6. No database. 7. No
custom APIs. 8. No cloud synchronization. 9. Local JSON/assets and local
device storage only. 10. WhatsApp and phone call handoff for orders. 11.
Both delivery and takeaway flows. 12. Honest handling of all offline and
synchronization limitations.

Implementation rules: - Do not silently omit requirements. - Do not
invent restaurant information. - Use placeholder data only when clearly
identified. - Build reusable, maintainable components. - Validate all
user input and imported files. - Handle errors and empty states. - Keep
customer and admin app data independent. - Do not claim that orders are
confirmed when they have only been handed off. - Do not claim that admin
edits automatically update customer devices. - Test the complete user
flows before delivering.

Deliver working source code for both apps, the required assets, tests,
setup instructions and Android build instructions.

**Final objective:** Deliver a polished, usable, frontend-only Android
food ordering ecosystem for Rajput Fast Food, consisting of a Customer
App and a separate Admin App, without a backend, database or custom
APIs.
