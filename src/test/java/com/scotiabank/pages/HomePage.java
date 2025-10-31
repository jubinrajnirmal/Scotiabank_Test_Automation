package com.scotiabank.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scotiabank.utils.BrowserUtils;

public class HomePage extends BrowserUtils{

	@FindBy(xpath ="//img[contains(@alt,'Scotiabank desktop logo')]")
	private WebElement homePageLogo;
	
	@FindBy(xpath="//*[contains(@id,'mega-menu')]/div/ul/li")
	private List<WebElement> listOfMenu;
	
	@FindBy(xpath="//*[contains(@id,'dd4')]/ul/li/ul/li")
	private List<WebElement> listOfSubMenu;
	
	private WebElement selectedMenu;
	
	
	public boolean verifyHomePageLogoIsDisplayed() {
		return homePageLogo.isDisplayed();
	}
	
	public void hoverOverSelectedMenuOption(String menu) {
		for (WebElement menuItem : listOfMenu) {
			String menuItemName = menuItem.findElement(By.xpath("./a/span")).getText();
			System.out.println(menuItemName);
			if(menuItemName.equals(menu)) {
				System.out.println("selected menu: " + menu);
				selectedMenu=menuItem;
				hover(menuItem);
				return;
			}
		}
		System.out.println("Error! Menu Item not found : " + menu);
	}
	
	public void selectSubMenuItem(String subMenuItem) {
		
		List<WebElement> subMenuItems = selectedMenu.findElements(By.xpath(".//div/ul/li/ul/li"));
		for (WebElement subItems : subMenuItems) {
			WebElement subMenuName = subItems.findElement(By.xpath(".//span"));
			System.out.println(subMenuName.getText());
			if(subMenuName.getText().equals(subMenuItem)) {
				subMenuName.findElement(By.xpath("./ancestor::a")).click();
				return;
			}
			
		}
		
	}
}
