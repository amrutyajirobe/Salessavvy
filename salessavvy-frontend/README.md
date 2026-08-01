# SalesSavvy Frontend

Feature-first React structure aligned with the SalesSavvy backend and the supplied UI designs.

## Screenshot component placement

- Login and registration: `src/features/auth`
- Customer homepage, category navigation, product list, and product cards: `src/features/products`
- Cart icon and cart page: `src/features/cart`
- Profile dropdown and profile page: `src/features/users`
- Header and layouts: `src/components/layout`
- Shared buttons, inputs, dialogs, and loaders: `src/components/ui`

## Backend alignment

- Auth: `POST /auth/login`, `POST /api/users/register`
- Products: `GET /api/products`, `GET /api/products/{productId}`
- Admin products: create, update, and delete under `/admin/products`
- Users: reserved for `/api/users/**`; the profile controller is currently empty
- Cart, orders, and payments: gateway routes exist, but controllers are not implemented yet

API modules should use one gateway client in `src/lib/api`, targeting `http://localhost:8080` locally. Login requests must send credentials because the JWT is stored in the HTTP-only `authToken` cookie.

The screenshots mention logout and cart-count endpoints, but those endpoints do not currently exist in the backend. Do not wire them until their contracts are implemented.
