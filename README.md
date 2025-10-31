# Scotiabank Test Automation Framework

Selenium + Java + Cucumber (JUnit) UI test suite, built with Maven.  
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 128 128"><path fill="#CF0A2C" d="M83.1 80.5c-4.7-.1-8.8 3.4-9.3 8.1 0 .2.1.3.3.3h18c.2 0 .3-.1.3-.3-.4-4.8-4.5-8.4-9.3-8.1z"/><path fill="#CF0A2C" d="M121.7 19.9l-38.4 43c-.4.5-1.2.5-1.7.1l-.1-.1-19.4-20.1c-.4-.4-.4-1-.1-1.5l6.5-8.3c.4-.5 1.1-.7 1.6-.3.1.1.2.1.2.2l11 12.1c.4.5 1.2.5 1.7.1l.1-.1 30.7-41.7c.3-.4.2-.9-.2-1.2-.1-.1-.3-.1-.5-.2H5.7c-.5.1-.9.5-.9 1v122.2c0 .5.4.9.9.9h116.6c.5 0 .9-.4.9-.9V20.5c0-.5-.4-.8-.8-.8-.3-.1-.5 0-.7.2zm-83.8 92.5c-7.7.3-15.2-2.3-20.9-7.4-.4-.4-.5-1-.1-1.5l4.5-6.4c.4-.5 1.1-.6 1.6-.3l.1.1c4.2 3.9 9.6 6 15.3 6 6 0 8.9-2.8 8.9-5.7 0-9.1-29.5-2.8-29.5-22.1 0-8.5 7.4-15.6 19.4-15.6 6.9-.2 13.7 2.1 19.1 6.5.4.4.5 1.1.1 1.5l-4.7 6.2c-.4.5-1.1.6-1.6.2-4-3.2-8.9-4.9-14-4.8-4.7 0-7.3 2.1-7.3 5.1 0 8.1 29.4 2.7 29.4 21.8.1 9.3-6.6 16.4-20.3 16.4zm64.3-17.8c0 .6-.5 1-1 1H74.3c-.2 0-.3.1-.3.3.9 5.2 5.6 8.8 10.9 8.5 3.4-.1 6.6-1.1 9.3-3.1.4-.3 1.1-.3 1.4.2l.1.1 3.3 4.8c.3.4.2 1-.2 1.4-4.3 3.2-9.6 4.8-14.9 4.6-11.6 0-20.3-7.8-20.3-20-.3-10.7 8.1-19.6 18.8-19.9h.9c11.3 0 19.1 8.5 19.1 20.9l-.2 1.2z"/></svg>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 128 128"><path fill="#00A818" d="M92.2 8.3c-1-.6-2-1.2-3-1.7l-3.3-1.5c-.4-.1-.7-.3-1.1-.4-1-.4-1.9-.8-2.9-1.1C76.5 1.8 70.8.9 64.8.9 34.3.9 9.7 25.6 9.7 56.1c0 26.9 19.2 49.2 44.5 54.2v15.8c32.9-5 62.1-31.2 64.3-65.6 1.3-20.8-9-42-26.3-52.2zM51.6 21.6c1.8-.2 3.8.5 5.1 2.1 1 1 1.6 2.3 2.3 3.7 2 4.7 1.3 9.8-1.4 13.5-4.7-1-9-4-11-8.7-.7-1.3-1.1-3.1-1.1-4.4.1-3.4 3-5.9 6.1-6.2zM35.1 37.9h1.1c1.7 0 3 .4 4.7 1.1 4.7 2 8.1 6 9.1 10.4-4 2.7-9.5 3.4-14.2 1.4-1.3-.7-2.6-1.4-4-2.4-4.4-3.6-2-10.3 3.3-10.5zm1.1 34.7c-6.1.3-9-6.7-4.6-10.4 1-1 2.3-1.7 4-2.4 1.8-.8 3.6-1.1 5.5-1.2 3.1-.1 6.1.8 8.6 2.5-.7 4.4-4 8.4-8.7 10.4-1.5.8-3.2 1.1-4.8 1.1zm23.2 9.8c-.7 1.3-1.3 2.7-2.3 4-3.4 4.4-11.2 1.3-10.8-4.4 0-1.3.4-3 1.1-4.3 2-4.7 6-7.7 10.7-8.7 2.6 4 3.3 9.1 1.3 13.4zm9.1-55c.7-1.3 1.3-2.7 2.3-4 1.4-1.6 3.4-2.3 5.3-2.1 3.1.3 6.1 2.8 5.9 6.4 0 1.3-.4 3.1-1.1 4.4-2 4.7-6 7.7-10.7 8.7-3-3.6-3.7-8.6-1.7-13.4zm3 59c-1-1-1.6-2.3-2.3-3.7-2-4.7-1.3-9.8 1.4-13.5 4.7 1 9 4 11 8.7.7 1.3 1.1 3.1 1.1 4.4.2 5.2-7.5 8.2-11.2 4.1zm20.6-14.1c-1.7 0-3-.4-4.7-1.1-4.7-2-8.1-6-9.1-10.4 2.5-1.7 5.6-2.6 8.7-2.5 1.8 0 3.7.4 5.5 1.2 1.3.7 2.6 1.3 4 2.3 4.6 3.4 1.6 10.9-4.4 10.5z"/></svg>
<img width="512" height="512" alt="image" src="https://github.com/user-attachments/assets/fb168ad6-1988-494c-89c3-7b35863e9533" />

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

## 🧩 Tech Stack Logos

<p align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/selenium/selenium-original.svg" height="40" alt="Selenium"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="Java"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/apachemaven/apachemaven-original.svg" height="40" alt="Maven"/>
  <img src="https://cucumber.io/wp-content/uploads/2021/05/cucumber-open-graph.png" height="40" alt="Cucumber"/>
</p>
