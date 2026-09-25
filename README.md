# Plataforma de Pagos E-Commerce Multinacional (Demo)

Sistema en **Java puro (solo JDK, sin frameworks)** que simula una
plataforma de e-commerce que opera en **EE. UU.**, la **Unión Europea** y
**Latinoamérica**, cada una con su propia pasarela de pago, motor de
impuestos y reglas de fraude. Incluye un front-end simple en HTML (sin
CSS) para probarlo desde el navegador.

El código, los comentarios y la documentación técnica principal están en
inglés (ver `README.md`); este archivo es solo una guía rápida en español.

## Patrones de diseño implementados

- **Abstract Factory** (`RegionalPaymentFactory`): según la región
  (`LATAMPaymentFactory`, `EUPaymentFactory`, `USPaymentFactory`) crea el
  trío de objetos correspondiente: pasarela de pago, generador de
  facturas y validador de fraude.
- **Bridge** (`PaymentTransaction` / `PaymentGateway`): separa *el tipo de
  pago* (`SubscriptionPayment` con reintentos, `OneTimePayment` directo)
  de *la pasarela* que efectivamente cobra (`PaymentGateway`).
- **Adapter** (`MercadoPagoAdapter`, `StripeAdapter`,
  `LocalBankISOAdapter`): traducen la interfaz estándar hacia cada SDK
  externo simulado (MercadoPago, Stripe y un socket bancario ISO 8583).

## Mapeo por región

| Región | Fábrica              | Pasarela (Adapter)     | Impuesto           |
|--------|----------------------|-------------------------|---------------------|
| LATAM  | `LATAMPaymentFactory` | MercadoPago             | IVA por país         |
| EU     | `EUPaymentFactory`    | Banco local (ISO 8583)  | IVA, con inversión de sujeto pasivo si hay NIF/VAT |
| US     | `USPaymentFactory`    | Stripe                  | Sales Tax fijo (7%)  |

## Estructura del proyecto

```
ecommerce-payment-system/
├── README.md          # documentación completa en inglés
├── README.es.md        # este archivo
├── run.sh               # compila y ejecuta todo
├── frontend/index.html # interfaz web (HTML sin CSS)
└── src/com/ecommerce/payment/
    ├── Main.java        # punto de entrada
    ├── region/          # país / región
    ├── gateway/         # PaymentGateway (Bridge)
    ├── external/        # SDKs de terceros simulados
    ├── adapter/         # los tres Adapters
    ├── transaction/     # PaymentTransaction y refinamientos (Bridge)
    ├── fraud/           # validadores de fraude
    ├── invoice/         # generadores de factura
    ├── factory/         # Abstract Factory
    ├── service/         # orquestador que integra todo
    ├── util/            # utilidades (JSON sin dependencias)
    └── web/             # servidor HTTP embebido + front-end
```

## Cómo ejecutarlo

Requiere un JDK (no solo JRE) versión 17 o superior.

```bash
./run.sh
```

Esto compila el proyecto, imprime en consola el escenario del caso de
estudio (pago por suscripción desde Colombia) y levanta el servidor en:

```
http://localhost:8080
```

Ahí puedes abrir el formulario web y probar distintos países y tipos de
pago, o llamar la API directamente:

```bash
curl -X POST http://localhost:8080/api/pay \
  -H "Content-Type: application/json" \
  -d '{
        "country": "CO",
        "paymentType": "SUBSCRIPTION",
        "amount": "29.99",
        "currency": "COP",
        "customerId": "CUST-001",
        "customerName": "Camila Restrepo",
        "customerEmail": "camila@example.com",
        "customerTaxId": "",
        "cardLast4": "4242",
        "subscriptionId": "SUB-COL-001"
      }'
```

## Ideas para probar en el front-end

- **Colombia + Suscripción** → MercadoPago, IVA 19%.
- **Alemania + Pago único** → banco local ISO 8583, IVA 19%.
- **Estados Unidos + Suscripción** → Stripe, Sales Tax 7%.
- **México con monto > 2000** → bloqueado por el validador de fraude.
- **Francia con NIF/Tax ID** → factura con inversión de sujeto pasivo (IVA 0).
