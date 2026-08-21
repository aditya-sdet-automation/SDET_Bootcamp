# SDET Bootcamp & TestNG Architecture

A dedicated automation sandbox for finalizing a production-grade web telemetry bot and experimenting with TestNG framework logic.

**Core Components:**
* **The Final Amazon Bot (Test 8):** A fully decoupled, data-driven tracker. It implements a TestNG `@DataProvider` for CSV data injection, completely bypasses UI search bars for direct ASIN navigation, and utilizes an Aggregator Pattern to send summarized SMTP email alerts.
* **TestNG Sandboxes:** Isolated test environments built to experiment with TestNG lifecycle annotations (`@BeforeMethod`, `@AfterMethod`) and foundational test assertions.
* **Cron Readiness:** The primary bot architecture is stripped of artificial thread sleeps, allowing it to execute at maximum speed via external Linux cron daemons.
