package com.example.demotest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GmapTest extends JUnitHTMLReporter {
    private AppiumDriver driver;
    private WebDriverWait webDriverWait;
    List<MobileElement> listOfResults;
    String resName;
    MobileElement element;

    @Before
    public void setup() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.1");
        desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Moto");
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.google.android.apps.maps");
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.google.android.maps.MapsActivity");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
        webDriverWait = new WebDriverWait(driver, 30);
    }

    @Test
    public void testSearchAddress_whenSearchLeinfeldenFasanen_shouldGiveOnlyOneSuggestion() {

        //Click on search box
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_text_box").click();

        //Type "leinfelden fasanen"
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_edit_text").sendKeys("leinfelden fasanen");

        List<WebElement> suggestions = driver.findElements(By.className("android.widget.RelativeLayout"));//("android.widget.TextView"));
        //It will return 2 suggestions, so this test will be failed.
        //Size=2 because one suggestion + "Choose on map"
        Assert.assertEquals(suggestions.size(), 2);
    }

    @Test
    public void testSearchAddress_whenSearchLeinfeldenFasanen_shouldGiveTwoReasonableSuggestions() {

        //Click on search box
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_text_box").click();

        //Type "leinfelden fasanen"
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_edit_text").sendKeys("leinfelden fasanen");

        //Should return 2 suggestions, one for "Fasanenhof", one for "Fasanenweg"
        List<WebElement> suggestions = driver.findElements(By.className("android.widget.TextView"));
        List<String> suggestionName = new ArrayList<>();
        for (WebElement suggestion : suggestions) {
            suggestionName.add(suggestion.getText());
        }
        Assert.assertTrue(suggestionName.contains("Fasanenhof") && suggestionName.contains("Fasanenweg"));
    }

    @Test
    public void testSearchAddress_whenSearchLeinfeldenFasanenAndSelectTheFirst_shouldGoToFasanenhof() {

        //Click on search box
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_text_box").click();

        //Type "leinfelden fasanen"
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_edit_text").sendKeys("leinfelden fasanen");

        //Select the first suggestion: "Fasanenhof"
        List<WebElement> listOfResults = driver.findElements(By.className("android.widget.RelativeLayout"));
        listOfResults.get(0).click();

        //Should go to "Fasanenhof, Leinfelden-Echterdingen, Germany"
        List<WebElement> results = driver.findElements(By.className("android.widget.TextView"));
        List<String> resultTexts = new ArrayList<>();
        for (WebElement result : results) {
            resultTexts.add(result.getText());
        }
        Assert.assertTrue(resultTexts.contains("Fasanenhof, Leinfelden, Germany"));
        Assert.assertFalse(resultTexts.contains("Fasanenweg, Leinfelden-Echterdingen, Germany"));

    }

    @Test
    public void testSearchAddress_whenSearchLeinfeldenFasanenAndSelectTheSecond_shouldGoToFasanenweg() {

        //Click on search box
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_text_box").click();

        //Type "leinfelden fasanen"
        driver.findElementById("com.google.android.apps.maps:id/search_omnibox_edit_text").sendKeys("leinfelden fasanen");

        //Select the second suggestion: "Fasanenweg"
        List<WebElement> listOfResults = driver.findElements(By.className("android.widget.RelativeLayout"));
        listOfResults.get(1).click();

        //Should go to "Fasanenweg, Leinfelden-Echterdingen, Germany"
        List<WebElement> results = driver.findElements(By.className("android.widget.TextView"));
        List<String> resultTexts = new ArrayList<>();
        for (WebElement result : results) {
            resultTexts.add(result.getText());
        }
        Assert.assertFalse(resultTexts.contains("Fasanenhof, Leinfelden, Germany"));
        Assert.assertTrue(resultTexts.contains("Fasanenweg, Leinfelden, Germany"));

    }

    @After
    public void mapTesttearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
