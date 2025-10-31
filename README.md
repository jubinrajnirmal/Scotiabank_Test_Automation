# Scotiabank Test Automation Framework

Selenium + Java + Cucumber (JUnit) UI test suite, built with Maven.  
<img width="50" height="50" alt="image" src="https://github.com/user-attachments/assets/f9de8cd0-3a29-4ca6-9055-0e096781411a" /> <img width="50" height="50" alt="image" src="https://github.com/user-attachments/assets/0956f7b2-ca82-4602-891e-b697608c07ca" /> <img width="50" height="50" alt="image" src="https://github.com/user-attachments/assets/fb168ad6-1988-494c-89c3-7b35863e9533" />  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="50" width="50" alt="Java"/>


---

## 🛠️ Prerequisites

- Java 17+ (check: `java -version`)
- Maven 3.8+ (check: `mvn -v`)
- Chrome or Firefox installed

---

## 🚀 How to Run (locally)

From the project root:

```bash
mvn clean test
```
---

## 🧪 CI / Jenkins

Repo includes a `Jenkinsfile` and is connected via GitHub webhook — any push triggers the pipeline automatically.

- In CI, `DriverUtils` enforces headless, CI-compliant execution for stability on agents.
- WebDriver binaries are handled by **WebDriverManager** in CI as well.

**Typical Jenkins step:**

```bash
mvn -B clean test
```

---

