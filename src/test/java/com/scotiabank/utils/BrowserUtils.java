package com.scotiabank.utils;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrowserUtils extends DriverUtils {

	// Configuration for waits
	private static final int DEFAULT_WAIT = 5;
	private WebDriverWait explicitWait;

	public BrowserUtils() {
		if (driver == null)
			driver = getDriver();
		PageFactory.initElements(driver, this);
		this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
	}

	// Explicit Wait
	public static void explicitWait(double seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	public void setExplicitWait(long seconds) {
		this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
	}

	// Implicit Wait
	public void implicitWait(long seconds) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
	}

	// Common expected conditions
	public void waitForAlertIsPresent() {
		explicitWait.until(ExpectedConditions.alertIsPresent());
	}

	public void waitForElementToBeClickable(WebElement element) {
		explicitWait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitForElementToBeSelected(WebElement element) {
		explicitWait.until(ExpectedConditions.elementToBeSelected(element));
	}

	public void waitForTextToBePresentInElement(WebElement element, String text) {
		explicitWait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForElementSelectionStateToBe(WebElement element, boolean selected) {
		explicitWait.until(ExpectedConditions.elementSelectionStateToBe(element, selected));
	}

	public void waitForFrameToBeAvailableAndSwitchToIt(String frameLocator) {
		explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	public void waitForInvisibilityOfElementLocated(By locator) {
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForInvisibilityOfElementWithText(By locator, String text) {
		explicitWait.until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
	}

	public void waitForPresenceOfAllElementsLocatedBy(By locator) {
		explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	public void waitForPresenceOfElementLocated(By locator) {
		explicitWait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public void waitForTextToBePresentInElementLocated(By locator, String text) {
		explicitWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	public void waitForTextToBePresentInElementValue(WebElement element, String text) {
		explicitWait.until(ExpectedConditions.textToBePresentInElementValue(element, text));
	}

	public void waitForTitleIs(String title) {
		explicitWait.until(ExpectedConditions.titleIs(title));
	}

	public void waitForTitleContains(String titlePart) {
		explicitWait.until(ExpectedConditions.titleContains(titlePart));
	}

	public void waitForVisibilityOf(WebElement element) {
		explicitWait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForVisibilityOfAllElements(List<WebElement> elements) {
		explicitWait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	public void waitForVisibilityOfAllElementsLocatedBy(By locator) {
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public void waitForVisibilityOfElementLocated(By locator) {
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void waitForElementToBeClickable(By locator) {
		explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForUrlChangeFrom(String oldUrl) {
		explicitWait.until(d -> !d.getCurrentUrl().equals(oldUrl));
	}

	// change Windows
	public String getCurrentWindowHandle() {
		return driver.getWindowHandle();
	}

	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}

	public Set<String> getAllOpenWindowHandles() {
		return driver.getWindowHandles();
	}

	public void openNewTab() {
		driver.switchTo().newWindow(WindowType.TAB);
	}

	public void openNewWindow() {
		driver.switchTo().newWindow(WindowType.WINDOW);
	}

	public void changeToOriginalWindow() {
		driver.switchTo().window(getCurrentWindowHandle());
	}

	public void closeWindowOrTab() {
		driver.close();
	}

	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public void minimizeWindow() {
		driver.manage().window().minimize();
	}

	// navigation
	public void driverGet(String url) {
		driver.get(url);
	}

	public void navigateToUrl(String url) {
		driver.navigate().to(url);
	}

	public void navigateBackward() {
		driver.navigate().back();
	}

	public void navigateForward() {
		driver.navigate().forward();
	}

	public void refreshBrowser() {
		driver.navigate().refresh();
	}

	// Screenshot Taker

	// WebsiteTrustCookiePromptCanceller

	public void closeCookieBannerIfPresent() {
		WebDriverWait w = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
		By closeBtn = By.xpath("//*[contains(@class,'onetrust-close-btn-ui')]");
		WebElement btn = w.until(ExpectedConditions.elementToBeClickable(closeBtn));
		btn.click();
		w.until(ExpectedConditions.invisibilityOfElementLocated(By.id("onetrust-banner-sdk")));
	}

	// Action Class Helper Methods
	public void scrollToFooter() {
		new Actions(driver).scrollByAmount(0, 10000).scrollByAmount(0, 1000).perform();
	}

	public void hover(WebElement element) {
		new Actions(driver).moveToElement(element).perform();
		;
	}

	// ---- JS helpers ----
	private JavascriptExecutor js() {
		return (JavascriptExecutor) driver;
	}

	/** Scroll the window by a small delta (defaults shown). */
	public void jsScrollBy(int x, int y) {
		js().executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
	}

	/** Scroll down a bit (nice for nudging content into view). */
	public void jsScrollDown() {
		jsScrollBy(0, 500); // adjust the 250 if you want a smaller/larger nudge
	}

	/** Scroll up a bit. */
	public void jsScrollUp() {
		jsScrollBy(0, -250);
	}

	/**
	 * Scroll element into view (centered, with small offset for sticky headers).
	 */
	public void jsScrollIntoViewCenter(WebElement el) {
		js().executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
		// optional micro-nudge in case of sticky top bars
		jsScrollBy(0, -60);
	}

}
