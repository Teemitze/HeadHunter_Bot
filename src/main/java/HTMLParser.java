import configuration.ConfigurationHHBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryVacancy;

import java.util.HashMap;
import java.util.Map;

class HTMLParser {
    private long countEmployee = RepositoryVacancy.countOfferDay();

    private static final Logger log = LoggerFactory.getLogger(HTMLParser.class);

    private Map<String, String> getAllEmployeesOnPage(WebDriver driver) {
        Document document = Jsoup.parse(driver.getPageSource());
        Elements elements = document.getElementsByClass("resume-search-item__content-wrapper");
        Map<String, String> employeesLink = new HashMap<>();
        for (Element element : elements) {
            if (getLinkEmployee(element) != null) {
                employeesLink.put(getLinkEmployee(element), getNameEmployee(element));
            }
        }
        return employeesLink;
    }

    private Map<String, String> getUniqueEmployees(Map<String, String> linkEmployees) {
        RepositoryVacancy repositoryVacancy = new RepositoryVacancy();
        Map<String, String> uniqueEmployeesLink = new HashMap<>();
        for (Map.Entry employee : linkEmployees.entrySet()) {
            if (repositoryVacancy.findVacancy(getUUIDEmployeeFromURL(String.valueOf(employee.getKey()))) == null) {
                uniqueEmployeesLink.put(String.valueOf(employee.getKey()), String.valueOf(employee.getValue()));
            }
        }
        return uniqueEmployeesLink;
    }

    private String getLinkEmployee(Element element) {
        String link = element.getElementsByClass("bloko-link_dimmed").attr("href");
        if (!link.isEmpty()) {
            return Main.HH + link;
        }
        return null;
    }

    private String getNameEmployee(Element element) {
        return element.getElementsByClass("resume-search-item__fullname").text();
    }

    void parseUniqueEmployees(WebDriver driver, Browser browser) {

        for (int i = ConfigurationHHBot.START_PAGE - 1; i <= ConfigurationHHBot.END_PAGE; i++) {
            driver.get(browser.getWebPageWithEmployees(i));
            if (isPageFound(driver)) {

                Map<String, String> uniqueEmployeesLink = getUniqueEmployees(getAllEmployeesOnPage(driver));

                for (Map.Entry<String, String> uniqueLink : uniqueEmployeesLink.entrySet()) {
                    String UUIDEmployeeFromURL = getUUIDEmployeeFromURL(uniqueLink.getKey());
                    driver.get(uniqueLink.getKey());
                    if (countEmployee == ConfigurationHHBot.MAX_LIMIT_SEND_OFFER) {
                        log.warn("Indicated count people were invited!");
                        driver.quit();
                        System.exit(0);
                    }
                    RepositoryVacancy repositoryVacancy = new RepositoryVacancy();
                    try {
                        browser.sendOffer(driver);
                        repositoryVacancy.addVacancy(UUIDEmployeeFromURL);
                    } catch (NoSuchElementException | TimeoutException e) {
                        try {
                            log.warn("Warning, element not found! Try repeating send offer " + Main.HH + "/resume/" + UUIDEmployeeFromURL, e);
                            driver.get(driver.getCurrentUrl());
                            browser.sendOffer(driver);
                            repositoryVacancy.addVacancy(UUIDEmployeeFromURL);
                            log.info("Employee " + Main.HH + "/resume/" + UUIDEmployeeFromURL + " invited!", e);
                        } catch (NoSuchElementException | TimeoutException e2) {
                            log.warn("Warning, element not found again! This element will be skipped!", e2);
                            continue;
                        }
                    }
                    countEmployee++;
                }
            } else {
                log.warn("Page not found!");
                driver.quit();
                System.exit(0);
            }
        }
    }

    private boolean isPageFound(WebDriver driver) {
        return !driver.findElement(By.className("header")).getText().equals("Ошибка 404");
    }

    private static String getUUIDEmployeeFromURL(String url) {
        return url.substring(51);
    }
}