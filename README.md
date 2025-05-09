# Playtorium-Take-home-assignment

# Discount Calculation Module

This Spring Boot project implements a discount engine that calculates the final price of items in a cart based on a list of discount campaigns.

## ✅ Features
- Supports multiple discount types (`Coupon`, `On Top`, `Seasonal`)
- Prevents duplicate campaigns within the same category
- Applies campaigns in the correct order: **Coupon → On Top → Seasonal**
- Resilient to invalid input with meaningful error messages

---

## 📦 How to Use

Send a `POST` request to:

```
POST /api/discount/calculate
```

### 🧾 Request JSON Format

```json
{
  "items": [
    { "name": "T-Shirt", "category": "Clothing", "price": 300 }
  ],
  "discounts": [
    {
      "category": "Coupon",
      "campaigns": {
        "name": "Fixed Amount",
        "parameters": { "Amount": 50 }
      }
    }
  ]
}
```

### ✅ Response Format

```json
{
  "finalPrice": 250,
  "message": "Discount applied successfully"
}
```

---

## 🧪 Supported Campaigns

| Category   | Campaign Name                          | Parameters Example                                              |
|------------|----------------------------------------|-----------------------------------------------------------------|
| Coupon     | `Fixed Amount`                         | `"Amount": 100`                                                 |
| Coupon     | `Percentage discount`                  | `"Percentage": 10`                                              |
| On Top     | `Percentage discount by item category` | `"Category": "Clothing", "Amount": 15`                          |
| On Top     | `Discount by points`                   | `"CustomerPoints": 100` (Max allowed = 20% of total cart price) |
| Seasonal   | `Seasonal`                             | `"Every": 300, "Discount": 50`                                  |

---

## ⚠️ Rules

- Only **one campaign per category** is allowed (e.g., only one Coupon)
- Campaigns are applied in this order:  
  `Coupon → On Top → Seasonal`
- Points discount is capped at 20% of the original total
- Seasonal discount is applied based on thresholds (e.g., 30 THB every 300 THB)

---

## ▶️ Running the Project

To run this Spring Boot application:

```bash
./mvnw spring-boot:run
```

Or in IntelliJ IDEA, run `DiscountApplication.java`

---

## 🌐 Swagger UI

Once the app is running, open your browser and go to:

```
http://localhost:8080/swagger-ui/index.html
```

You can test the API and try different discount combinations from this interface.

---

## 👨‍💻 Developer

- **Wannachat Srisawat**
- Submission for **Playtorium Take-home Assignment**
